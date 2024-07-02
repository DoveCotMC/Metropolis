package team.dovecotmc.metropolis.block;

import mtr.data.Station;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.item.ItemCard;
import team.dovecotmc.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTurnstile extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public BlockTurnstile() {
        super(Settings.of(Material.METAL).nonOpaque());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        if (world.getBlockEntity(pos) instanceof BlockEntityTurnstile blockEntity && !state.get(OPEN)) {
            Station station = MtrStationUtil.getStationByPos(pos, world);
            if (station == null) {
                player.sendMessage(Text.translatable("info.metropolis.turnstile_error"), true);
                return ActionResult.SUCCESS;
            }

            ItemStack stack = player.getStackInHand(Hand.MAIN_HAND);
            NbtCompound nbt = blockEntity.createNbt();

            if (stack.getItem() == mtr.Items.BRUSH.get()) {
                if (blockEntity.type != BlockEntityTurnstile.EnumTurnstileType.ENTER && blockEntity.getItems().isEmpty()) {
                    player.sendMessage(Text.translatable("info.metropolis.unable_switch_turnstile_type"), true);
                    return ActionResult.SUCCESS;
                }
                nbt.putInt(BlockEntityTurnstile.TURNSTILE_TYPE, (nbt.getInt(BlockEntityTurnstile.TURNSTILE_TYPE) + 1) % 3);
                player.sendMessage(Text.translatable("info.metropolis.turnstile_type", nbt.getInt(BlockEntityTurnstile.TURNSTILE_TYPE)), true);
                blockEntity.readNbt(nbt);
                blockEntity.clear();
                ((ServerPlayerEntity) player).networkHandler.sendPacket(blockEntity.toUpdatePacket());
                for (int i = 0; i < blockEntity.size(); i++) {
                    MetroServerNetwork.removeInventoryItem(i, pos, (ServerPlayerEntity) player);
                }
                return ActionResult.SUCCESS;
            }

            if (blockEntity.type == BlockEntityTurnstile.EnumTurnstileType.ENTER) {
                if (!blockEntity.getStack(0).isEmpty()) {
                    if (world.getTime() - nbt.getLong(BlockEntityTurnstile.TICKET_ANIMATION_START) >= 7) {
                        // TODO: Take ticket and open the gate
                        player.giveItemStack(blockEntity.getStack(0));
                        blockEntity.removeStack(0);

                        ((ServerPlayerEntity) player).networkHandler.sendPacket(blockEntity.toUpdatePacket());
                        MetroServerNetwork.removeInventoryItem(0, pos, (ServerPlayerEntity) player);

                        world.setBlockState(pos, state.with(OPEN, true));
                        world.createAndScheduleBlockTick(pos, this, 40);
                    }
                }

                if (stack.getOrCreateNbt().contains(ItemTicket.ENTERED_STATION) || stack.getOrCreateNbt().contains(ItemTicket.ENTERED_ZONE)) {
                    player.sendMessage(Text.translatable("info.metropolis.to_service_center"), true);
                    return ActionResult.SUCCESS;
                }
                if (stack.getItem() instanceof ItemTicket) {
                    NbtCompound stackNbt = stack.getOrCreateNbt();
                    stackNbt.putString(ItemTicket.ENTERED_STATION, station.name);
                    stackNbt.putInt(ItemTicket.ENTERED_ZONE, station.zone);

                    blockEntity.setStack(0, stack);
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.createNbt();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getTime());
                    blockEntity.readNbt(nbt);
                } else if (stack.getItem() instanceof ItemCard) {
                    // TODO: Cards
                }
            } else if (blockEntity.type == BlockEntityTurnstile.EnumTurnstileType.EXIT) {
                NbtCompound stackNbt = stack.getOrCreateNbt();
                if (!stackNbt.contains(ItemTicket.ENTERED_STATION) && !stackNbt.contains(ItemTicket.ENTERED_ZONE) || Math.abs(station.zone - stackNbt.getInt(ItemTicket.ENTERED_ZONE)) + 1 < stackNbt.getInt(ItemTicket.BALANCE)) {
                    player.sendMessage(Text.translatable("info.metropolis.to_service_center"), true);
                    return ActionResult.SUCCESS;
                }
                if (stack.getItem() instanceof ItemTicket) {
                    blockEntity.setStack(0, stack);
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.createNbt();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getTime());
                    blockEntity.readNbt(nbt);

                    world.setBlockState(pos, state.with(OPEN, true));
                    world.createAndScheduleBlockTick(pos, this, 40);
                } else if (stack.getItem() instanceof ItemCard) {
                    // TODO: Cards
                }
            }
            ((ServerPlayerEntity) player).networkHandler.sendPacket(blockEntity.toUpdatePacket());
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(OPEN, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        return VoxelShapes.union(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, -4, 3, 17, 20, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, -4, 16, 17, 20, facing)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        VoxelShape shape = VoxelShapes.union(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, -4, 2, 24, 20, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, -4, 16, 24, 20, facing)
        );
        return state.get(OPEN) ? shape : VoxelShapes.union(shape, MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, facing));
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(OPEN, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPEN);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTurnstile(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
