package team.dovecotmc.metropolis.block.platform_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import team.dovecotmc.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoorController;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public class BlockPlatformDoorControllerBox extends HorizontalDirectionalBlock implements EntityBlock {
    public static final int MAX_DETECTION_RADIUS = 512;

    public static final BooleanProperty ATTACHED_ON_WALL = BooleanProperty.create("attached");

    public BlockPlatformDoorControllerBox(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (level.getBlockEntity(blockPos) instanceof BlockEntityPlatformDoorController blockEntity) {
            if (level.getGameTime() - blockEntity.lastToggleTime <= 20) {
                return InteractionResult.SUCCESS;
            }

            blockEntity.openState = !blockEntity.openState;
            blockEntity.lastToggleTime = level.getGameTime();

            scanPlatformDoors(level, blockPos, blockState, blockEntity.openState, blockEntity.lastToggleTime);

            level.blockEntityChanged(blockPos);
        }

        return InteractionResult.SUCCESS;
    }

    private void scanPlatformDoors(Level level, BlockPos blockPos, BlockState blockState, boolean openState, long lastToggleTime) {
        BlockPos platformPos = null;

        if (level.getBlockState(blockPos.below()).getBlock() instanceof IBlockPlatform) {
            platformPos = blockPos.below();
        } else if (level.getBlockState(blockPos.below().below()).getBlock() instanceof IBlockPlatform) {
            platformPos = blockPos.below().below();
        } else {
            for (Direction facing : Direction.allShuffled(level.getRandom())) {
                if (level.getBlockState(blockPos.relative(facing).below().below()).getBlock() instanceof IBlockPlatform) {
                    platformPos = blockPos.relative(facing).below().below();
                    break;
                } else if (level.getBlockState(blockPos.relative(facing).below()).getBlock() instanceof IBlockPlatform) {
                    platformPos = blockPos.relative(facing).below();
                    break;
                }
            }
        }

        if (platformPos == null)
            return;

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();

        visited.add(platformPos);
        queue.add(platformPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();

            // Block proc
            if (level.getBlockState(current.above()).getBlock() instanceof AbstractBlockPlatformDoor doorBlock) {
                BlockPos doorPos = current.above();
                doorBlock.setOpenState(level, doorPos, openState);
            }

            // Four connected directions
            for (Direction direction : new Direction[] {
                    Direction.NORTH,
                    Direction.SOUTH,
                    Direction.WEST,
                    Direction.EAST
            }) {
                BlockPos next = current.relative(direction);

                // Skip if checked
                if (visited.contains(next))
                    continue;

                // Range check
                int distance = Math.abs(next.getX() - platformPos.getX())
                        + Math.abs(next.getZ() - platformPos.getZ());

                if (distance > MAX_DETECTION_RADIUS)
                    continue;

                // Type check
                if (!(level.getBlockState(next).getBlock() instanceof IBlockPlatform))
                    continue;

                visited.add(next);
                queue.add(next);
            }
        }
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
