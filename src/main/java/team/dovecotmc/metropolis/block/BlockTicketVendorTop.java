package team.dovecotmc.metropolis.block;

import mtr.Items;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
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
import team.dovecotmc.metropolis.util.MetroBlockUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockTicketVendorTop extends HorizontalFacingBlock {
    public static final Map<Integer, BlockTicketVendorTop> TYPES = new HashMap<>();
    public final int id;

    public BlockTicketVendorTop() {
        super(Settings.of(Material.METAL).nonOpaque().luminance(value -> 0));

        this.id = TYPES.size();
        TYPES.put(id, this);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!hand.equals(Hand.MAIN_HAND)) {
            return ActionResult.PASS;
        }

        if (player.getStackInHand(hand).getItem().equals(Items.BRUSH.get())) {
            int id = ((BlockTicketVendorTop) state.getBlock()).id;
            world.setBlockState(pos, TYPES.get((id + 1) % (TYPES.size())).getDefaultState().with(FACING, state.get(FACING)));
            world.playSound(null, pos, SoundEvents.BLOCK_COPPER_BREAK, SoundCategory.BLOCKS, 1f, 1f);
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
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

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (world.getBlockState(pos.down()).getBlock() instanceof BlockTicketVendor) {
            world.breakBlock(pos.down(), true);
        }
    }
}
