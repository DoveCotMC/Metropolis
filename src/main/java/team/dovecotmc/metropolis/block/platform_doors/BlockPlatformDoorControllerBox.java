package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoorController;

public class BlockPlatformDoorControllerBox extends HorizontalDirectionalBlock implements EntityBlock {
    public static final BooleanProperty ATTACHED_ON_WALL = BooleanProperty.create("attached");

    public BlockPlatformDoorControllerBox(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level instanceof ServerLevel serverLevel && level.getBlockEntity(blockPos) instanceof BlockEntityPlatformDoorController blockEntity) {
            blockEntity.openState = !blockEntity.openState;
            blockEntity.lastToggleTime = level.getGameTime();

            serverLevel.blockEntityChanged(blockPos);
        }

        return InteractionResult.SUCCESS;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(ATTACHED_ON_WALL, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(ATTACHED_ON_WALL);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityPlatformDoorController(blockPos, blockState);
    }
}
