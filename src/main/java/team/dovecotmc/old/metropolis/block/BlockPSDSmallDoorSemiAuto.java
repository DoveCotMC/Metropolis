package team.dovecotmc.old.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.mtr.core.tool.Utilities;
import org.mtr.mod.block.PlatformHelper;
import team.dovecotmc.old.metropolis.block.entity.BlockEntityPSDSmallDoorSemiAuto;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.old.metropolis.block.interfaces.IBlockMTRPlatformDoor;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockPSDSmallDoorSemiAuto extends HorizontalDirectionalBlock implements EntityBlock, IBlockMTRPlatformDoor {
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

    public BlockPSDSmallDoorSemiAuto(Properties settings) {
        super(settings.noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
//        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
//        if (itemStack.getItem() instanceof DyeItem dyeItem) {
//            if (level.isClientSide())
//                return InteractionResult.SUCCESS;
//
//            if (level.getBlockEntity(blockPos) instanceof BlockEntityPSDSmallDoorSemiAuto blockEntity) {
//                blockEntity.tint = dyeItem.getDyeColor().getTextColor();
//                blockEntity.setChanged();
////                System.out.println("COLOR: " + Integer.toHexString(blockEntity.tint));
//
////                for (Player nPlayer : level.players()) {
////                    if (nPlayer instanceof ServerPlayer serverPlayer) {
////                        serverPlayer.connection.send(blockEntity.getUpdatePacket());
////                    }
////                }
//            }
//        }

        return InteractionResult.PASS;
    }

    @Override
    public void setOpenState(boolean open, float doorValue, Level level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof BlockEntityPSDSmallDoorSemiAuto entity) {
            entity.open = Utilities.clamp(doorValue, 0.0F, 1.0F);
        }

//        if (true)
//            return;

//        if (level.getBlockEntity(pos) instanceof BlockEntityPSDSmallDoorSemiAuto entity) {
//            final float val0 = entity.open;
//            entity.open = doorValue;
//            if (val0 > doorValue) {
//                if (state.getValue(OPEN) && doorValue <= 0.4f) {
//                    entity.animationStartTime = level.getGameTime();
//                    level.setBlockAndUpdate(pos, state.setValue(OPEN, false));
//                }
//            } else if (val0 < doorValue) {
//                if (state.getValue(OPEN) != open) {
//                    entity.animationStartTime = level.getGameTime();
//                    level.setBlockAndUpdate(pos, state.setValue(OPEN, open));
//                }
//            }
//
//            entity.setChanged();
//            for (Player player : level.players()) {
//                if (player instanceof ServerPlayer serverPlayer) {
//                    serverPlayer.connection.send(entity.getUpdatePacket());
//                }
//            }
//        } else {
//            level.setBlockAndUpdate(pos, state.setValue(OPEN, false));
//        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 5,
                16, 16, 8,
                state.getValue(FACING)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (world.getBlockEntity(pos) instanceof BlockEntityPSDSmallDoorSemiAuto entity && entity.getLevel() != null) {
            if (entity.getLevel().isClientSide() && entity.open > 0) {
                return Shapes.empty();
            } else {
                return entity.getLevel().isClientSide() ? MetroBlockUtil.getVoxelShapeByDirection(
                        0, 0, 5,
                        16, 24, 8,
                        state.getValue(FACING)
                ) : Shapes.empty();
            }
        } else {
            return Shapes.empty();
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(state, world, pos, random);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).getBlock() instanceof IBlockPlatform || world.getBlockState(pos.below()).getBlock() instanceof PlatformHelper;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!canSurvive(state, world, pos)) {
            world.destroyBlock(pos, true);
        }
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
//        System.out.println(ctx.getHitPos());
        double offsetX = ctx.getClickLocation().x() - ctx.getClickedPos().getX();
        double offsetZ = ctx.getClickLocation().z() - ctx.getClickedPos().getZ();
        BlockPos pos = ctx.getClickedPos();
        Level world = ctx.getLevel();
        Direction facing = ctx.getHorizontalDirection();
        boolean flipped = false;

        if (world.getBlockState(pos.relative(facing.getCounterClockWise())).getBlock() instanceof BlockPSDSmallDoorSemiAuto) {
            BlockState state = world.getBlockState(pos.relative(facing.getCounterClockWise()));
            if (state.getValue(FLIPPED)) {
                flipped = false;
            }
        } else if (world.getBlockState(pos.relative(facing.getClockWise())).getBlock() instanceof BlockPSDSmallDoorSemiAuto) {
            BlockState state = world.getBlockState(pos.relative(facing.getClockWise()));
            if (!state.getValue(FLIPPED)) {
                flipped = true;
            }
        } else {
            if (facing.equals(Direction.NORTH)) {
                flipped = offsetX > 0.5;
            } else if (facing.equals(Direction.SOUTH)) {
                flipped = offsetX < 0.5;
            } else if (facing.equals(Direction.WEST)) {
                flipped = offsetZ < 0.5;
            } else if (facing.equals(Direction.EAST)) {
                flipped = offsetZ > 0.5;
            }
        }

        return this.defaultBlockState().setValue(FACING, facing).setValue(OPEN, false).setValue(FLIPPED, flipped);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPEN).add(FLIPPED);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityPSDSmallDoorSemiAuto(pos, state);
    }
}
