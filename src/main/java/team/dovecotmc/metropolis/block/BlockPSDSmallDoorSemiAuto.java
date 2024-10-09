package team.dovecotmc.metropolis.block;

import mtr.data.Platform;
import mtr.data.RailwayData;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntityPSDSmallDoorSemiAuto;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatformDoor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockPSDSmallDoorSemiAuto extends BlockHorizontalFacing implements BlockEntityProvider, IBlockPlatform, IBlockPlatformDoor {
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public BlockPSDSmallDoorSemiAuto(Settings settings) {
        super(settings.nonOpaque());
    }

    @Override
    public void setOpenState(boolean open, float openValue, World world, BlockPos pos, BlockState state) {
        if (world.getBlockEntity(pos) instanceof BlockEntityPSDSmallDoorSemiAuto entity) {
            final float val0 = entity.open;
            entity.open = openValue;
            if (val0 > openValue) {
                if (state.get(OPEN) != open) {
//                    System.out.println("114514: Close");
                    entity.animationStartTime = world.getTime();
                    world.setBlockState(pos, state.with(OPEN, open));
                }
            } else if (val0 < openValue) {
                if (state.get(OPEN) != open) {
//                    System.out.println("1145141919810: Open");
                    entity.animationStartTime = world.getTime();
                    world.setBlockState(pos, state.with(OPEN, open));
                }
            }
            for (PlayerEntity player : world.getPlayers()) {
                if (player instanceof ServerPlayerEntity serverPlayer) {
                    serverPlayer.networkHandler.sendPacket(entity.toUpdatePacket());
                }
            }
        } else {
            world.setBlockState(pos, state.with(OPEN, false));
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
//        if (world.getBlockEntity(pos) instanceof BlockEntityPSDSmallDoorSemiAuto entity && entity.open) {
//            return VoxelShapes.empty();
//        }
        return state.get(OPEN) ? VoxelShapes.empty() : super.getCollisionShape(state, world, pos, context);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.setOpenState(false, 0, world, pos, state);
        super.scheduledTick(state, world, pos, random);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return super.getPlacementState(ctx).with(OPEN, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityPSDSmallDoorSemiAuto(pos, state);
    }
}
