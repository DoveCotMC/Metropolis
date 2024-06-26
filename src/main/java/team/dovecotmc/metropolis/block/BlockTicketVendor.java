package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityTicketVendor;
import team.dovecotmc.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTicketVendor extends HorizontalFacingBlock implements BlockEntityProvider {
    public final boolean isFunctional;
    public final Block defaultUpper;

    public BlockTicketVendor(boolean isFunctional) {
        this(isFunctional, MetroBlocks.BLOCK_TICKET_VENDOR_UP_1);
    }

    public BlockTicketVendor(boolean isFunctional, Block defaultUpper) {
        super(Settings.of(Material.METAL).nonOpaque().luminance(value -> 0));
        this.isFunctional = isFunctional;
        this.defaultUpper = defaultUpper;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
//        return VoxelShapes.combine(
//                IBlock.getVoxelShapeByDirection(0.0, 0.0, 10.0, 16.0, 16.0, 16.0, facing),
//                IBlock.getVoxelShapeByDirection(0.0, 0.0, 4.0, 16.0 , 10.0, 16.0, facing),
//                BooleanBiFunction.OR);
        return MetroBlockUtil.getVoxelShapeByDirection(0.0, 0.0, 4.0, 16.0 , 16.0, 16.0, facing);
    }

//    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
//        return world.getBlockState(pos).isAir() && world.getBlockState(pos.up()).isAir();
//    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if (world.getBlockState(pos.up()).isAir()) {
            world.setBlockState(pos.up(), defaultUpper.getDefaultState().with(FACING, state.get(FACING)));
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (world.getBlockState(pos.up()).getBlock() instanceof BlockTicketVendorUp) {
            world.breakBlock(pos.up(), false);
        }
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand interactionHand, BlockHitResult blockHitResult) {
        if (!this.isFunctional) {
            return ActionResult.PASS;
        }

        Vec3d clickPos = blockHitResult.getPos().add(new Vec3d(-pos.getX(), -pos.getY(), -pos.getZ()));
//        System.out.println(blockHitResult.getPos().add(new Vec3d(-pos.getX(), -pos.getY(), -pos.getZ())));

        if (!world.isClient) {
            BlockEntityTicketVendor blockEntity = world.getBlockEntity(pos, MetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY).orElse(null);

            System.out.println(clickPos.y);
            if (clickPos.y > 0.4d) {
                MetroServerNetwork.openTicketVendorScreen(world, pos, (ServerPlayerEntity) player);
            } else {
                if (blockEntity != null && !blockEntity.getStack(0).isEmpty()) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    serverPlayer.getInventory().insertStack(blockEntity.getStack(0));
                    blockEntity.removeStack(0);
                    serverPlayer.networkHandler.sendPacket(blockEntity.toUpdatePacket());
                    MetroServerNetwork.removeInventoryItem(0, pos, serverPlayer);
                    return ActionResult.SUCCESS;
                } else if (blockEntity != null && player.getStackInHand(Hand.MAIN_HAND).getItem().equals(MetroItems.ITEM_CARD)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    blockEntity.setStack(1, player.getStackInHand(Hand.MAIN_HAND));
                    serverPlayer.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                    NbtCompound nbt = blockEntity.createNbt();
                    nbt.putLong(BlockEntityTicketVendor.CARD_ANIMATION_BEGIN_TIME, world.getTime());
                    blockEntity.readNbt(nbt);

                    serverPlayer.networkHandler.sendPacket(blockEntity.toUpdatePacket());
                    return ActionResult.SUCCESS;
                } else if (blockEntity != null && !blockEntity.getStack(1).isEmpty()) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    serverPlayer.getInventory().insertStack(blockEntity.getStack(1));
                    blockEntity.removeStack(1);
                    serverPlayer.networkHandler.sendPacket(blockEntity.toUpdatePacket());
                    MetroServerNetwork.removeInventoryItem(1, pos, serverPlayer);
                    return ActionResult.SUCCESS;
                }else {
                    MetroServerNetwork.openTicketVendorScreen(world, pos, (ServerPlayerEntity) player);
                }
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTicketVendor(pos, state);
    }
}
