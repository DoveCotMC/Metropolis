package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityTrainStopSign;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.old.metropolis.util.MtrCommonUtil;

public class BlockTrainStopSign extends HorizontalDirectionalBlock implements EntityBlock {
    public BlockTrainStopSign(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        // TODO: Placeholder item
//        if (player instanceof ServerPlayer serverPlayer && player.getItemInHand(InteractionHand.MAIN_HAND).getItem().equals(MtrCommonUtil.getBrushItem())) {
//            if (level.getBlockEntity(blockPos) instanceof BlockEntityTrainStopSign blockEntityTrainStopSign) {
//                blockEntityTrainStopSign.length = Math.max(blockEntityTrainStopSign.length + (player.isShiftKeyDown() ? -1 : 1), 0) % 17;
//                serverPlayer.connection.send(blockEntityTrainStopSign.getUpdatePacket());
//            }
//        }

        return InteractionResult.PASS;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Direction facing = blockState.getValue(FACING);
        return Shapes.or(
                MetroBlockUtil.getVoxelShapeByDirection(
                        6, 0, 6,
                        10, 16, 10,
                        facing
                ),
                MetroBlockUtil.getVoxelShapeByDirection(
                        3, 3, 5.5,
                        13, 13, 8,
                        facing
                )
        );
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BlockEntityTrainStopSign(blockPos, blockState);
    }
}
