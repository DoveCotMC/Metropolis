package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class AbstractBlockPlatformFence extends HorizontalDirectionalBlock {
    public static final IntegerProperty TINT_COLOR = IntegerProperty.create("tint_color", 0, 15);

    public AbstractBlockPlatformFence(Properties properties) {
        super(properties);
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(TINT_COLOR, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TINT_COLOR);
    }
}
