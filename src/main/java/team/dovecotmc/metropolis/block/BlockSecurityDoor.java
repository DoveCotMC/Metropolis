package team.dovecotmc.metropolis.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.util.MtrSoundUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockSecurityDoor extends HorizontalDirectionalBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final BooleanProperty OPEN;
//    public static final EnumProperty<Direction.Axis> AXIS;

    public BlockSecurityDoor() {
        super(Properties.of(Material.METAL, DyeColor.LIGHT_GRAY).strength(6.0f).noOcclusion());
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, placer, itemStack);
        world.setBlockAndUpdate(pos.above(), this.withPropertiesOf(state).setValue(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.above()).isAir();
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        if (!world.isClientSide()) {
            if (state.getValue(HALF).equals(DoubleBlockHalf.LOWER) && !state.getValue(OPEN)) {
                if (entity instanceof Player player) {
                    boolean open = true;

                    // Block with inventory
                    for (ItemStack stack : player.getInventory().items) {
                        if (stack.hasTag()) {
                            CompoundTag nbt = stack.getOrCreateTag();
                            if (nbt.contains("BlockEntityTag", CompoundTag.TAG_COMPOUND)) {
                                CompoundTag blockNbt = nbt.getCompound("BlockEntityTag");
                                if (blockNbt.contains("Items", CompoundTag.TAG_LIST)) {
                                    ListTag itemsList = blockNbt.getList("Items", Tag.TAG_COMPOUND);
                                    for (Tag itemNbtRaw : itemsList) {
                                        if (itemNbtRaw instanceof CompoundTag itemNbt) {
                                            if (itemNbt.contains("id", Tag.TAG_STRING)) {
                                                if (Metropolis.config.dangerItems.contains(itemNbt.getString("id"))) {
                                                    open = false;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (Metropolis.config.dangerItems.contains(Registry.ITEM.getKey(stack.getItem()).toString())) {
                            open = false;
                            break;
                        }
                    }
                    if (open) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundSource.BLOCKS, 1f, 1f);
                    } else {
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.has_danger_item"), true);
                    }
                    world.setBlockAndUpdate(pos, state.setValue(OPEN, open));
                    world.scheduleTick(pos, state.getBlock(), 20);
                }
            }
        }
    }

    @Override
    public void destroy(LevelAccessor world, BlockPos pos, BlockState state) {
        super.destroy(world, pos, state);
        if (state.getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
            if (world.getBlockState(pos.below()).getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
                world.destroyBlock(pos.below(), false);
            }
        } else if (state.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
            if (world.getBlockState(pos.above()).getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
                world.destroyBlock(pos.above(), false);
            }
        }
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        return Shapes.or(MetroBlockUtil.getVoxelShapeByDirection(
                0, 0, 0,
                1, 16, 16,
                state.getValue(FACING)
        ), MetroBlockUtil.getVoxelShapeByDirection(
                15, 0, 0,
                16, 16, 16,
                state.getValue(FACING)
        ));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        if (state.getValue(HALF).equals(DoubleBlockHalf.UPPER)) {
            return Shapes.or(Shapes.or(MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    1, 16, 16,
                    state.getValue(FACING)
            ), MetroBlockUtil.getVoxelShapeByDirection(
                    15, 0, 0,
                    16, 16, 16,
                    state.getValue(FACING)
            )), MetroBlockUtil.getVoxelShapeByDirection(
                    0, 15, 0,
                    16, 19, 16,
                    state.getValue(FACING)
            ));
        } else if (state.getValue(HALF).equals(DoubleBlockHalf.LOWER)) {
            return Shapes.or(MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    1, 16, 16,
                    state.getValue(FACING)
            ), MetroBlockUtil.getVoxelShapeByDirection(
                    15, 0, 0,
                    16, 16, 16,
                    state.getValue(FACING)
            ));
        } else {
            return Shapes.empty();
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        VoxelShape shape = super.getCollisionShape(state, world, pos, context);
        if (state.getValue(HALF).equals(DoubleBlockHalf.LOWER) && !state.getValue(OPEN)) {
            return state.getValue(OPEN) ?
                    shape :
                    Shapes.or(
                            shape,
                            MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, state.getValue(FACING))
                    );
        }
        return shape;
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(HALF).add(OPEN);
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlockAndUpdate(pos, state.setValue(OPEN, false));
    }

    static {
        HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
        OPEN = BooleanProperty.create("open");
//        AXIS = EnumProperty.of("axis", Direction.Axis.class);
    }
}
