package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;

public abstract class AbstractBlockPlatformDoor extends AbstractBlockPlatformFence implements EntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public AbstractBlockPlatformDoor(Properties properties) {
        super(properties);
    }

    public void setOpenState(Level level, BlockPos blockPos, boolean openState) {
        BlockState doorState = level.getBlockState(blockPos);
        BlockEntity rawEntity = level.getBlockEntity(blockPos);

        if (doorState.getValue(AbstractBlockPlatformDoor.OPEN) != openState) {
            if (rawEntity instanceof BlockEntityPlatformDoor blockEntity) {
                blockEntity.setBindingBlock(blockPos);
                blockEntity.setLastToggleTime(level.getGameTime());

                level.blockEntityChanged(blockPos);
                level.setBlockAndUpdate(blockPos, doorState.setValue(AbstractBlockPlatformDoor.OPEN, openState));
                level.setBlockAndUpdate(blockPos.above(), level.getBlockState(blockPos.above()).setValue(AbstractBlockPlatformDoor.OPEN, openState));
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx);

        if (state == null)
            return null;

        return state.setValue(OPEN, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF).equals(DoubleBlockHalf.LOWER))
            return new BlockEntityPlatformDoor(blockPos, blockState);

        return null;
    }
}
