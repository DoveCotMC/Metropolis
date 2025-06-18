package team.dovecotmc.metropolis.block;

import mtr.Items;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockTicketVendorUp extends HorizontalDirectionalBlock {
    public static final Map<Integer, BlockTicketVendorUp> TYPES = new HashMap<>();
    public final int id;

    public BlockTicketVendorUp() {
        super(Properties.of(Material.METAL).strength(6.0f).noOcclusion().lightLevel(value -> 0));

        this.id = TYPES.size();
        TYPES.put(id, this);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!hand.equals(InteractionHand.MAIN_HAND)) {
            return InteractionResult.PASS;
        }

        if (player.getItemInHand(hand).getItem().equals(Items.BRUSH.get())) {
            int id = ((BlockTicketVendorUp) state.getBlock()).id;
            world.setBlockAndUpdate(pos, TYPES.get((id + 1) % (TYPES.size())).defaultBlockState().setValue(FACING, state.getValue(FACING)));
            world.playSound(null, pos, SoundEvents.COPPER_BREAK, SoundSource.BLOCKS, 1f, 1f);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return MetroBlockUtil.getVoxelShapeByDirection(0.0, 0.0, 9.0, 16.0 , 16.0, 16.0, facing);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
        if (world.getBlockState(pos.below()).getBlock() instanceof BlockTicketVendor || world.getBlockState(pos.below()).getBlock() instanceof BlockFareAdjMachine) {
            world.destroyBlock(pos.below(), true);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.below()).getBlock() instanceof BlockTicketVendor || world.getBlockState(pos.below()).getBlock() instanceof BlockFareAdjMachine ? new ItemStack(world.getBlockState(pos.below()).getBlock()) : ItemStack.EMPTY;
    }
}
