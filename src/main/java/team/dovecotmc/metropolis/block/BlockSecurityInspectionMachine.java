package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.block.entity.BlockEntitySecurityInspectionMachine;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.util.MtrSoundUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockSecurityInspectionMachine extends HorizontalDirectionalBlock implements EntityBlock {
    public static final int PROCESS_DURATION = 40;
    public static final EnumProperty<EnumBlockSecurityInspectionMachinePart> PART = EnumProperty.create("part", EnumBlockSecurityInspectionMachinePart.class);

    public BlockSecurityInspectionMachine() {
        super(Properties.of(Material.METAL, DyeColor.LIGHT_GRAY).strength(6.0f));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        BlockEntity entityRaw = null;
        if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD)) {
            entityRaw = world.getBlockEntity(pos.relative(state.getValue(FACING).getOpposite()));
        } else if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
            entityRaw = world.getBlockEntity(pos.relative(state.getValue(FACING)));
        } else if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            entityRaw = world.getBlockEntity(pos);
        }

        if (entityRaw instanceof BlockEntitySecurityInspectionMachine entity) {
            if (entity.getItem(0).isEmpty()) {
                CompoundTag nbt = entity.saveWithoutMetadata();
                nbt.putLong(BlockEntitySecurityInspectionMachine.ITEM_ANIMATION_TIME, world.getGameTime());
                entity.load(nbt);
                entity.setItem(0, player.getItemInHand(InteractionHand.MAIN_HAND));
                ((ServerPlayer) player).connection.send(entity.getUpdatePacket());
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                world.scheduleTick(entity.getBlockPos(), entity.getBlockState().getBlock(), PROCESS_DURATION);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        boolean danger = false;
        if (world.getBlockEntity(pos) instanceof BlockEntitySecurityInspectionMachine entity) {
            if (!entity.getItem(0).isEmpty()) {

                // Block with inventory
                if (entity.getItem(0).hasTag()) {
                    CompoundTag nbt = entity.getItem(0).getOrCreateTag();
                    if (nbt.contains("BlockEntityTag", CompoundTag.TAG_COMPOUND)) {
                        CompoundTag blockNbt = nbt.getCompound("BlockEntityTag");
                        if (blockNbt.contains("Items", CompoundTag.TAG_LIST)) {
                            ListTag itemsList = blockNbt.getList("Items", Tag.TAG_COMPOUND);
                            for (Tag itemNbtRaw : itemsList) {
                                if (itemNbtRaw instanceof CompoundTag itemNbt) {
                                    if (itemNbt.contains("id", Tag.TAG_STRING)) {
                                        if (Metropolis.config.dangerItems.contains(itemNbt.getString("id"))) {
                                            danger = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (Metropolis.config.dangerItems.contains(Registry.ITEM.getKey(entity.getItem(0).getItem()).toString())) {
                    danger = true;
                }

                if (danger) {
                    // TODO: Custom sound
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundSource.BLOCKS, 1f, 1f);
                }

                Direction facing = state.getValue(FACING);
                BlockPos dropPos = pos.relative(facing.getOpposite());
                ItemEntity itemEntity = new ItemEntity(world, dropPos.getX(), dropPos.getY(), dropPos.getZ(), entity.getItem(0));
                itemEntity.setPosRaw(itemEntity.getX() + 0.5, itemEntity.getY() + 0.5, itemEntity.getZ() + 0.5);
                facing = facing.getOpposite();
                itemEntity.push(facing.getStepX() * 0.1, 0, facing.getStepZ() * 0.1);
                world.addFreshEntity(itemEntity);

                for (ServerPlayer player : world.players()) {
                    entity.removeItemNoUpdate(0);
                    player.connection.send(entity.getUpdatePacket());
                    MetroServerNetwork.removeInventoryItem(0, pos, player);
                }
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        if (facing == null) {
            return false;
        }
        return world.getBlockState(pos.relative(facing)).isAir() && world.getBlockState(pos.relative(facing.getOpposite())).isAir();
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        Direction facing = state.getValue(FACING);
        if (world.getBlockState(pos.relative(facing)).isAir()) {
            world.setBlockAndUpdate(pos.relative(facing), this.withPropertiesOf(state).setValue(PART, EnumBlockSecurityInspectionMachinePart.HEAD));
        }
        if (world.getBlockState(pos.relative(facing.getOpposite())).isAir()) {
            world.setBlockAndUpdate(pos.relative(facing.getOpposite()), this.withPropertiesOf(state).setValue(PART, EnumBlockSecurityInspectionMachinePart.TAIL));
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
        if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            Direction facing = state.getValue(FACING);
            BlockState state1 = world.getBlockState(pos.relative(facing));
            if (state1.getBlock() instanceof BlockSecurityInspectionMachine && state1.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD)) {
                world.destroyBlock(pos.relative(facing), false);
            }
            BlockState state2 = world.getBlockState(pos.relative(facing.getOpposite()));
            if (state2.getBlock() instanceof BlockSecurityInspectionMachine && state2.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
                world.destroyBlock(pos.relative(facing.getOpposite()), false);
            }
        } else {
            if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD)) {
                world.destroyBlock(pos.relative(state.getValue(FACING).getOpposite()), true);
                world.destroyBlock(pos.relative(state.getValue(FACING).getOpposite()).relative(state.getValue(FACING).getOpposite()), true);
            } else if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
                world.destroyBlock(pos.relative(state.getValue(FACING)), true);
                world.destroyBlock(pos.relative(state.getValue(FACING)).relative(state.getValue(FACING)), true);
            }
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER) ?
                Shapes.block() : Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) || state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
            Direction facing = state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) ?
                    state.getValue(FACING) : state.getValue(FACING).getOpposite();
            return Shapes.or(MetroBlockUtil.getVoxelShapeByDirection(
                    1, 0, 2,
                    15, 12, 12,
                            facing
            ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 12,
                            16, 24, 16,
                            facing
                    ));
        } else if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    16, 24, 16,
                    state.getValue(FACING)
            );
        } else {
            return Shapes.block();
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) || state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.TAIL)) {
            Direction facing = state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.HEAD) ?
                    state.getValue(FACING) : state.getValue(FACING).getOpposite();
            return Shapes.or(MetroBlockUtil.getVoxelShapeByDirection(
                            1, 0, 2,
                            15, 12, 12,
                            facing
                    ),
                    MetroBlockUtil.getVoxelShapeByDirection(
                            0, 0, 12,
                            16, 24, 16,
                            facing
                    ));
        } else if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            return MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    16, 24, 16,
                    state.getValue(FACING)
            );
        } else {
            return Shapes.block();
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(PART, EnumBlockSecurityInspectionMachinePart.CENTER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(PART);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        if (state.getValue(PART).equals(EnumBlockSecurityInspectionMachinePart.CENTER)) {
            return new BlockEntitySecurityInspectionMachine(pos, state);
        }
        return null;
    }

    public enum EnumBlockSecurityInspectionMachinePart implements StringRepresentable {
        HEAD,
        CENTER,
        TAIL;

        @Override
        public String getSerializedName() {
            return this.toString().toLowerCase();
        }
    }
}
