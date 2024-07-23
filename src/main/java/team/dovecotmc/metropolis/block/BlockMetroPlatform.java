package team.dovecotmc.metropolis.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import team.dovecotmc.metropolis.IBlockPlatform;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockMetroPlatform extends HorizontalFacingBlock implements IBlockPlatform {
    public static final EnumProperty<EnumPlatformType> TYPE = EnumProperty.of("type", EnumPlatformType.class);

    public BlockMetroPlatform(Settings settings) {
        super(settings);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(TYPE, EnumPlatformType.NORMAL);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TYPE);
    }

    public enum EnumPlatformType implements StringIdentifiable {
        NORMAL,
        OUTER_CORNER_LEFT,
        OUTER_CORNER_RIGHT,
        INNER_CORNER_LEFT,
        INNER_CORNER_RIGHT;

        @Override
        public String asString() {
            return this.toString().toLowerCase();
        }
    }
}
