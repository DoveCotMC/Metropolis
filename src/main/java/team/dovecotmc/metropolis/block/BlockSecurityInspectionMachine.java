package team.dovecotmc.metropolis.block;

import net.fabricmc.loader.impl.lib.sat4j.minisat.constraints.cnf.OriginalBinaryClause;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.entity.BlockEntitySecurityInspectionMachine;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockSecurityInspectionMachine extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final int PROCESS_DURATION = 40;
    public static final EnumProperty<EnumBlockSecurityInspectionMachinePart> PART = EnumProperty.of("part", EnumBlockSecurityInspectionMachinePart.class);

    public BlockSecurityInspectionMachine() {
        super(Settings.of(Material.METAL, DyeColor.LIGHT_GRAY));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        BlockEntity entityRaw = null;
        if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD)) {
            entityRaw = world.getBlockEntity(pos.offset(state.get(FACING).getOpposite()));
        } else if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
            entityRaw = world.getBlockEntity(pos.offset(state.get(FACING)));
        } else if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            entityRaw = world.getBlockEntity(pos);
        }

        if (entityRaw instanceof BlockEntitySecurityInspectionMachine entity) {
            if (entity.getStack(0).isEmpty()) {
                NbtCompound nbt = entity.createNbt();
                nbt.putLong(BlockEntitySecurityInspectionMachine.ITEM_ANIMATION_TIME, world.getTime());
                entity.readNbt(nbt);
                entity.setStack(0, player.getStackInHand(Hand.MAIN_HAND));
                ((ServerPlayerEntity) player).networkHandler.sendPacket(entity.toUpdatePacket());
                player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                world.createAndScheduleBlockTick(entity.getPos(), entity.getCachedState().getBlock(), PROCESS_DURATION);
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (world.getBlockEntity(pos) instanceof BlockEntitySecurityInspectionMachine entity) {
//            NbtCompound nbt = entity.createNbt();
            if (!entity.getStack(0).isEmpty()) {
                // TODO: Improve drop
                Direction facing = state.get(FACING);
                BlockPos dropPos = pos.offset(facing.getOpposite());
                ItemEntity itemEntity = new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), entity.getStack(0));
                itemEntity.setPos(itemEntity.getX() + 0.5, itemEntity.getY() + 0.5, itemEntity.getZ() + 0.5);
                facing = facing.getOpposite();
                itemEntity.addVelocity(facing.getOffsetX() * 0.1, 0, facing.getOffsetZ() * 0.1);
                world.spawnEntity(itemEntity);

                for (ServerPlayerEntity player : world.getPlayers()) {
                    entity.removeStack(0);
                    player.networkHandler.sendPacket(entity.toUpdatePacket());
                    MetroServerNetwork.removeInventoryItem(0, pos, player);
                }
            }
        }
    }

    //    @Nullable
//    @Override
//    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
//        return (world1, pos, state1, blockEntity) -> {
//            if (!world1.isClient() && blockEntity instanceof BlockEntitySecurityInspectionMachine entity) {
//                NbtCompound nbt = entity.createNbt();
//                if (world1.getTime() - nbt.getLong(BlockEntitySecurityInspectionMachine.ITEM_ANIMATION_TIME) > PROCESS_DURATION) {
//                    if (!entity.getStack(0).isEmpty()) {
//                        // TODO: Improve drop
//                        Direction facing = state.get(FACING);
//                        BlockPos dropPos = pos.offset(facing.getOpposite());
//                        ItemEntity itemEntity = new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), entity.getStack(0));
//                        itemEntity.setPos(itemEntity.getX() + 0.5, itemEntity.getY() + 0.5, itemEntity.getZ() + 0.5);
//                        facing = facing.getOpposite();
//                        itemEntity.addVelocity(facing.getOffsetX() * 0.1, 0, facing.getOffsetZ() * 0.1);
//                        world.spawnEntity(itemEntity);
//
//                        for (ServerPlayerEntity player : ((ServerWorld) world1).getPlayers()) {
//                            entity.removeStack(0);
//                            player.networkHandler.sendPacket(entity.toUpdatePacket());
//                            MetroServerNetwork.removeInventoryItem(0, pos, player);
//                        }
//                    }
//                }
//            }
//        };
//    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        Direction facing = state.get(FACING);
        if (facing == null) {
            return false;
        }
        return world.getBlockState(pos.offset(facing)).isAir() && world.getBlockState(pos.offset(facing.getOpposite())).isAir();
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
                    0, 0, 0,
                    16, 24, 16,
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

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        if (state.get(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            return new BlockEntitySecurityInspectionMachine(pos, state);
        }
        return null;
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
