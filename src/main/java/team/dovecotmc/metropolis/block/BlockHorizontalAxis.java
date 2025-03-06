package team.dovecotmc.metropolis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

/**
 * @project Metropolis
 * @author Arrokoth
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockHorizontalAxis extends Block {
    public static final EnumProperty<Direction.Axis> AXIS;

    public BlockHorizontalAxis(Settings settings) {
        super(settings);
    }

    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
        return switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> switch (state.get(AXIS)) {
                case X -> state.with(AXIS, Direction.Axis.Z);
                case Z -> state.with(AXIS, Direction.Axis.X);
                default -> state;
            };
            default -> state;
        };
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(AXIS, ctx.getPlayerFacing().getAxis());
    }

    static {
        AXIS = Properties.AXIS;
    }
}
