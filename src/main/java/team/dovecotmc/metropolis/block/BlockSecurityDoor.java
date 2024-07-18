package team.dovecotmc.metropolis.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.util.MtrSoundUtil;

import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockSecurityDoor extends HorizontalFacingBlock {
    public static final EnumProperty<DoubleBlockHalf> HALF;
    public static final BooleanProperty OPEN;
//    public static final EnumProperty<Direction.Axis> AXIS;

    public BlockSecurityDoor() {
        super(Settings.of(Material.METAL, DyeColor.LIGHT_GRAY).nonOpaque());
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        world.setBlockState(pos.up(), this.getStateWithProperties(state).with(HALF, DoubleBlockHalf.UPPER));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient()) {
            if (state.get(HALF).equals(DoubleBlockHalf.LOWER) && !state.get(OPEN)) {
                boolean open = true;
                PlayerEntity player = (PlayerEntity) entity;
                System.out.println(Metropolis.config.dangerItems);
                for (ItemStack stack : player.getInventory().main) {
                    // TODO: Configurable
//                    if (stack.getItem().equals(Items.TNT)) {
//                        open = false;
//                    }
                    for (String id : Metropolis.config.dangerItems) {
                        if (Objects.equals(id, Registry.ITEM.getId(stack.getItem()).toString())) {
                            System.out.println(111);
                            open = false;
                            break;
                        }
                    }
//                    System.out.println();
//                    if (Metropolis.config.dangerItems.contains(Registry.ITEM.getId(stack.getItem()).toString())) {
//                    }
                }
                if (open) {
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundCategory.BLOCKS, 1f, 1f);
                } else {
                    player.sendMessage(Text.translatable("info.metropolis.has_danger_item"), true);
                }
                world.setBlockState(pos, state.with(OPEN, open));
                world.createAndScheduleBlockTick(pos, state.getBlock(), 20);
            }
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        super.onBroken(world, pos, state);
        if (state.get(HALF).equals(DoubleBlockHalf.UPPER)) {
            if (world.getBlockState(pos.down()).get(HALF).equals(DoubleBlockHalf.LOWER)) {
                world.breakBlock(pos.down(), false);
            }
        } else if (state.get(HALF).equals(DoubleBlockHalf.LOWER)) {
            if (world.getBlockState(pos.up()).get(HALF).equals(DoubleBlockHalf.UPPER)) {
                world.breakBlock(pos.up(), false);
            }
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(HALF).equals(DoubleBlockHalf.UPPER)) {
            VoxelShape shape = VoxelShapes.union(VoxelShapes.union(MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    1, 16, 16,
                    state.get(FACING)
            ), MetroBlockUtil.getVoxelShapeByDirection(
                    15, 0, 0,
                    16, 16, 16,
                    state.get(FACING)
            )), MetroBlockUtil.getVoxelShapeByDirection(
                    0, 15, 0,
                    16, 19, 16,
                    state.get(FACING)
            ));
            return shape;
//            return state.get(OPEN) ?
//                    shape :
//                    VoxelShapes.union(
//                            shape,
//                            MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, state.get(FACING))
//                    );
        } else if (state.get(HALF).equals(DoubleBlockHalf.LOWER)) {
            VoxelShape shape = VoxelShapes.union(MetroBlockUtil.getVoxelShapeByDirection(
                    0, 0, 0,
                    1, 16, 16,
                    state.get(FACING)
            ), MetroBlockUtil.getVoxelShapeByDirection(
                    15, 0, 0,
                    16, 16, 16,
                    state.get(FACING)
            ));
            return shape;
//            return state.get(OPEN) ?
//                    shape :
//                    VoxelShapes.union(
//                            shape,
//                            MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, state.get(FACING))
//                    );
        } else {
            return VoxelShapes.empty();
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape shape = super.getCollisionShape(state, world, pos, context);
        if (state.get(HALF).equals(DoubleBlockHalf.LOWER) && !state.get(OPEN)) {
            return state.get(OPEN) ?
                    shape :
                    VoxelShapes.union(
                            shape,
                            MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, state.get(FACING))
                    );
        }
        return shape;
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(HALF, DoubleBlockHalf.LOWER).with(OPEN, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(HALF).add(OPEN);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        world.setBlockState(pos, state.with(OPEN, false));
    }

    static {
        HALF = Properties.DOUBLE_BLOCK_HALF;
        OPEN = BooleanProperty.of("open");
//        AXIS = EnumProperty.of("axis", Direction.Axis.class);
    }
}
