package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityFareAdj;
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
public class BlockFareAdjMachine extends HorizontalFacingBlock implements BlockEntityProvider {
    public final Block defaultUpper;

    public BlockFareAdjMachine() {
        this(MetroBlocks.BLOCK_TICKET_VENDOR_UP_1);
    }

    public BlockFareAdjMachine(Block defaultUpper) {
        super(Settings.of(Material.METAL).nonOpaque().luminance(value -> 0));
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
        return MetroBlockUtil.getVoxelShapeByDirection(0.0, 0.0, 4.0, 16.0 , 16.0, 16.0, facing);
    }

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
        if (!world.isClient) {
            MetroServerNetwork.openFareAdjustmentScreen(pos, (ServerPlayerEntity) player);
//            BlockEntityFareAdj blockEntity = world.getBlockEntity(pos, MetroBlockEntities.FARE_ADJ_BLOCK_ENTITY).orElse(null);
//
//            if (blockEntity != null && !blockEntity.getStack(0).isEmpty()) {
//                world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
//                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
//                serverPlayer.getInventory().insertStack(blockEntity.getStack(0));
//                blockEntity.removeStack(0);
//                serverPlayer.networkHandler.sendPacket(blockEntity.toUpdatePacket());
//                MetroServerNetwork.removeInventoryItem(0, pos, serverPlayer);
//            } else if (blockEntity != null && player.getStackInHand(Hand.MAIN_HAND).getItem().equals(MetroItems.ITEM_CARD)) {
//                world.playSound(null, pos, SoundEvents.BLOCK_WOOL_PLACE, SoundCategory.BLOCKS, 1f, 1f);
//                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
//
//                serverPlayer.networkHandler.sendPacket(blockEntity.toUpdatePacket());
//                MetroServerNetwork.openTicketVendorChargeScreen(pos, (ServerPlayerEntity) player, player.getStackInHand(Hand.MAIN_HAND));
//            } else {
//                if (blockEntity != null) {
//                    blockEntity.removeStack(1);
//                    MetroServerNetwork.removeInventoryItem(1, pos, (ServerPlayerEntity) player);
//                    MetroServerNetwork.openTicketVendorScreen(pos, (ServerPlayerEntity) player, blockEntity.getStack(1));
//                }
//            }
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
        return new BlockEntityFareAdj(pos, state);
    }
}
