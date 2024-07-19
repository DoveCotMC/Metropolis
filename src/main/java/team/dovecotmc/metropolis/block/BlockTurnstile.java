package team.dovecotmc.metropolis.block;

import mtr.data.Station;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
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
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.item.ItemCard;
import team.dovecotmc.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.util.MtrCommonUtil;
import team.dovecotmc.metropolis.util.MtrSoundUtil;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockTurnstile extends HorizontalFacingBlock implements BlockEntityProvider, IBlockStationOverlayShouldRender {
    public static final BooleanProperty OPEN = BooleanProperty.of("open");
    public static final IntProperty TYPE = IntProperty.of("type", 0, 2);
    public static final BooleanProperty CONNECTED = BooleanProperty.of("connected");
    public final boolean icOnly;

    public BlockTurnstile(boolean icOnly) {
        super(Settings.of(Material.METAL).nonOpaque());
        this.icOnly = icOnly;
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

            BlockEntityTurnstile.EnumTurnstileType type = BlockEntityTurnstile.EnumTurnstileType.get(state.get(TYPE));
            if (stack.getItem() == MtrCommonUtil.getBrushItem()) {
                if (type != BlockEntityTurnstile.EnumTurnstileType.ENTER && blockEntity.getItems().isEmpty()) {
                    player.sendMessage(Text.translatable("info.metropolis.unable_switch_turnstile_type"), true);
                    return ActionResult.SUCCESS;
                }
                world.playSound(null, pos, SoundEvents.BLOCK_COPPER_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                int nextTypeIndex = (type.index + 1) % 2;
                world.setBlockState(pos, state.with(TYPE, nextTypeIndex));
                Text typeName = Text.translatable("misc.metropolis.turnstile_mode." + BlockEntityTurnstile.EnumTurnstileType.get(nextTypeIndex).name().toLowerCase());
                player.sendMessage(Text.translatable("info.metropolis.turnstile_type", typeName), true);
                blockEntity.readNbt(nbt);
                blockEntity.clear();
                ((ServerPlayerEntity) player).networkHandler.sendPacket(blockEntity.toUpdatePacket());
                for (int i = 0; i < blockEntity.size(); i++) {
                    MetroServerNetwork.removeInventoryItem(i, pos, (ServerPlayerEntity) player);
                }

                return ActionResult.SUCCESS;
            }

            if (type == BlockEntityTurnstile.EnumTurnstileType.ENTER) {
                if (!blockEntity.getStack(0).isEmpty()) {
                    if (world.getTime() - nbt.getLong(BlockEntityTurnstile.TICKET_ANIMATION_START) >= 7) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundCategory.BLOCKS, 1f, 1f);
                        world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                        player.giveItemStack(blockEntity.getStack(0));
                        blockEntity.removeStack(0);

                        ((ServerPlayerEntity) player).networkHandler.sendPacket(blockEntity.toUpdatePacket());
                        MetroServerNetwork.removeInventoryItem(0, pos, (ServerPlayerEntity) player);

                        world.setBlockState(pos, state.with(OPEN, true));
                        world.createAndScheduleBlockTick(pos, this, 40);
                        return ActionResult.SUCCESS;
                    }
                }

                // Enter
                if (stack.getItem() instanceof ItemTicket) {
                    if (icOnly) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(Text.translatable("info.metropolis.use_other_turnstile"), true);
                        return ActionResult.SUCCESS;
                    }

                    if (stack.getOrCreateNbt().contains(ItemTicket.ENTERED_STATION) || stack.getOrCreateNbt().contains(ItemTicket.ENTERED_ZONE)) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(Text.translatable("info.metropolis.to_service_center"), true);
                        return ActionResult.SUCCESS;
                    }

                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);

                    NbtCompound stackNbt = stack.getOrCreateNbt();
                    stackNbt.putString(ItemTicket.ENTERED_STATION, station.name);
                    stackNbt.putInt(ItemTicket.ENTERED_ZONE, station.zone);

                    ItemStack newStack = new ItemStack(MetroItems.ITEM_SINGLE_TRIP_TICKET_USED);
                    newStack.setNbt(stackNbt);
                    blockEntity.setStack(0, newStack);
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.createNbt();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getTime());
                    blockEntity.readNbt(nbt);
                } else if (stack.getItem() instanceof ItemCard) {
                    // TODO: Custom Cards sound
                    NbtCompound stackNbt = stack.getOrCreateNbt();
                    stackNbt.putString(ItemCard.ENTERED_STATION, station.name);
                    stackNbt.putInt(ItemCard.ENTERED_ZONE, station.zone);
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundCategory.BLOCKS, 1f, 1f);

                    world.setBlockState(pos, state.with(OPEN, true));
                    world.createAndScheduleBlockTick(pos, this, 40);
                }
            } else if (type == BlockEntityTurnstile.EnumTurnstileType.EXIT) {
                NbtCompound stackNbt = stack.getOrCreateNbt();
                if (!stackNbt.contains(ItemTicket.ENTERED_STATION) && !stackNbt.contains(ItemTicket.ENTERED_ZONE)) {
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundCategory.BLOCKS, 1f, 1f);
                    player.sendMessage(Text.translatable("info.metropolis.to_service_center"), true);
                    return ActionResult.SUCCESS;
                }

                if (stack.getItem() instanceof ItemTicket) {
                    if (icOnly) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundCategory.BLOCKS, 1f, 1f);
                        player.sendMessage(Text.translatable("info.metropolis.use_other_turnstile"), true);
                        return ActionResult.SUCCESS;
                    }

                    int cost = Math.abs(station.zone - stackNbt.getInt(ItemTicket.ENTERED_ZONE)) + 1;
                    int balance = stackNbt.getInt(ItemTicket.BALANCE);

                    if (balance < cost) {
                        player.sendMessage(Text.translatable("info.metropolis.no_enough_balance"), true);
                        return ActionResult.SUCCESS;
                    }

                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundCategory.BLOCKS, 1f, 1f);
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);

                    blockEntity.setStack(0, stack);
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.createNbt();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getTime());
                    blockEntity.readNbt(nbt);

                    world.setBlockState(pos, state.with(OPEN, true));
                    world.createAndScheduleBlockTick(pos, this, 40);
                } else if (stack.getItem() instanceof ItemCard) {
                    int cost = Math.abs(station.zone - stackNbt.getInt(ItemCard.ENTERED_ZONE)) + 1;
                    int balance = stackNbt.getInt(ItemCard.BALANCE);

                    if (balance < cost) {
                        player.sendMessage(Text.translatable("info.metropolis.no_enough_balance"), true);
                        return ActionResult.SUCCESS;
                    }

                    // TODO: Cards sound
                    stackNbt.remove(ItemCard.ENTERED_STATION);
                    stackNbt.remove(ItemCard.ENTERED_ZONE);
                    stackNbt.putInt(ItemCard.BALANCE, balance - cost);
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundCategory.BLOCKS, 1f, 1f);

                    world.setBlockState(pos, state.with(OPEN, true));
                    world.createAndScheduleBlockTick(pos, this, 40);
                }
            }
            // TODO: Direct debit
            /* else if (type == BlockEntityTurnstile.EnumTurnstileType.DIRECT_DEBIT) {
                NbtCompound stackNbt = stack.getOrCreateNbt();

                int cost = Math.abs(station.zone - stackNbt.getInt(ItemTicket.ENTERED_ZONE)) + 1;
                int balance = stackNbt.getInt(ItemTicket.BALANCE);

                if (balance < cost) {
                    player.sendMessage(Text.translatable("info.metropolis.no_enough_balance"), true);
                    return ActionResult.SUCCESS;
                }

                if (stack.getItem() instanceof ItemTicket) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
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
            }*/

            ((ServerPlayerEntity) player).networkHandler.sendPacket(blockEntity.toUpdatePacket());
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.get(FACING).rotateYClockwise();
        if (world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockTurnstile) {
            return state.with(CONNECTED, true);
        } else {
            return state.with(CONNECTED, false);
        }
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        Direction facing = state.get(FACING).rotateYClockwise();
        if (world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockTurnstile) {
            world.setBlockState(pos, state.with(CONNECTED, true));
        } else {
            world.setBlockState(pos, state.with(CONNECTED, false));
        }
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
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(OPEN, false).with(TYPE, 0).with(CONNECTED, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPEN).add(TYPE).add(CONNECTED);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTurnstile(pos, state);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        BlockEntityTurnstile.EnumTurnstileType type = BlockEntityTurnstile.EnumTurnstileType.get(state.get(TYPE));
        switch (type) {
            case ENTER -> {
                if (this.icOnly) {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_IC_ONLY_ENTER);
                } else {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_ENTER);
                }
            }
            case EXIT -> {
                if (this.icOnly) {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_IC_ONLY_EXIT);
                } else {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_EXIT);
                }
            }
        };
        return new ItemStack(MetroItems.ITEM_TURNSTILE_ENTER);
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }
}
