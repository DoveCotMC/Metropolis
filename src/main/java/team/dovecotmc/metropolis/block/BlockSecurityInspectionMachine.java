package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.DyeColor;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockSecurityInspectionMachine extends HorizontalFacingBlock {
    public static final EnumProperty<EnumBlockSecurityInspectionMachinePart> PART = EnumProperty.of("part", EnumBlockSecurityInspectionMachinePart.class);

    public BlockSecurityInspectionMachine() {
        super(Settings.of(Material.METAL, DyeColor.LIGHT_GRAY));
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        Direction facing = state.get(FACING);
        if (world.getBlockState(pos.offset(facing)).isAir()) {
            world.setBlockState(pos.offset(facing), this.getStateWithProperties(state).with(PART, EnumBlockSecurityInspectionMachinePart.HEAD));
        }
        if (world.getBlockState(pos.offset(facing.getOpposite())).isAir()) {
            world.setBlockState(pos.offset(facing.getOpposite()), this.getStateWithProperties(state).with(PART, EnumBlockSecurityInspectionMachinePart.TAIL));
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            Direction facing = state.get(FACING);
            BlockState state1 = world.getBlockState(pos.offset(facing));
            if (state1.getBlock() instanceof BlockSecurityInspectionMachine && state1.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD)) {
                world.breakBlock(pos.offset(facing), false);
            }
            BlockState state2 = world.getBlockState(pos.offset(facing.getOpposite()));
            if (state2.getBlock() instanceof BlockSecurityInspectionMachine && state2.get(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
                world.breakBlock(pos.offset(facing.getOpposite()), false);
            }
        } else {
            if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD)) {
                world.breakBlock(pos.offset(state.get(FACING).getOpposite()), true);
                world.breakBlock(pos.offset(state.get(FACING).getOpposite()).offset(state.get(FACING).getOpposite()), true);
            } else if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
                world.breakBlock(pos.offset(state.get(FACING)), true);
                world.breakBlock(pos.offset(state.get(FACING)).offset(state.get(FACING)), true);
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) || state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
            Direction facing = state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) ?
                    state.get(FACING) : state.get(FACING).getOpposite();
            return VoxelShapes.union(MetroBlockUtil.getVoxelShapeByDirection(
                    1, 0, 2,
                    15, 12, 12,
                            facing
            ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 12,
                            16, 24, 16,
                            facing
                    ));
        } else if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, -4,
                    16, 24, 20,
                    state.get(FACING)
            );
        } else {
            return VoxelShapes.fullCube();
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) || state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
            Direction facing = state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) ?
                    state.get(FACING) : state.get(FACING).getOpposite();
            return VoxelShapes.union(MetroBlockUtil.getVoxelShapeByDirection(
                            1, 0, 2,
                            15, 12, 12,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 12,
                            16, 24, 16,
                            facing
                    ));
        } else if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    16, 24, 16,
                    state.get(FACING)
            );
        } else {
            return VoxelShapes.fullCube();
        }
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(PART, EnumBlockSecurityInspectionMachinePart.CENTER);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(PART);
    }

    public enum EnumBlockSecurityInspectionMachinePart implements StringIdentifiable {
        HEAD,
        CENTER,
        TAIL;

        @Override
        public String asString() {
            return this.toString().toLowerCase();
        }
    }
}
