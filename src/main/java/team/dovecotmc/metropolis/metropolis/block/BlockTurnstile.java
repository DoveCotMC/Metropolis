package team.dovecotmc.metropolis.metropolis.block;

import mtr.SoundEvents;
import mtr.block.IBlock;
import mtr.data.RailwayData;
import mtr.data.Station;
import mtr.item.ItemRailModifier;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;

import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockTurnstile extends HorizontalFacingBlock implements BlockEntityProvider {
    public static final BooleanProperty OPEN = BooleanProperty.of("open");

    public BlockTurnstile() {
        super(Settings.of(Material.METAL, MapColor.GRAY).requiresTool().strength(2.0F).nonOpaque());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
//        if (world.isClient()) {
//            return ActionResult.PASS;
//        }
        return ActionResult.SUCCESS;

//        RailwayData railwayData = RailwayData.getInstance(world);
//        Station station = RailwayData.getStation(railwayData.stations, railwayData.dataCache, pos);
//
//        if (Objects.isNull(station)) {
//            return ActionResult.PASS;
//        }
//
//        ItemStack itemStack = player.getStackInHand(hand);
//        if (itemStack.getItem() instanceof ItemTicket) {
//            NbtCompound nbt = itemStack.getOrCreateNbt();
//            if (this.isExit) {
//                if (!nbt.getBoolean(ItemTicket.ENTERED)) {
//                    player.sendMessage(Text.translatable("metropolis.info.ticket_error"), true);
//                    world.playSound(null, pos, SoundEvents.TICKET_BARRIER, SoundCategory.BLOCKS, 1.0f, 1.0f);
//                    return ActionResult.SUCCESS;
//                }
//
//                if (nbt.getInt(ItemTicket.REMAIN_MONEY) < station.zone - nbt.getInt(ItemTicket.ENTERED_ZONE)) {
//                    player.sendMessage(Text.translatable("metropolis.info.ticket_error"), true);
//                    world.playSound(null, pos, SoundEvents.TICKET_BARRIER, SoundCategory.BLOCKS, 1.0f, 1.0f);
//                    return ActionResult.SUCCESS;
//                }
//
//                if (((ItemTicket) itemStack.getItem()).disposable) {
//                    player.setStackInHand(hand, ItemStack.EMPTY);
//                } else {
//                    nbt.putBoolean(ItemTicket.ENTERED, false);
//                    nbt.putInt(ItemTicket.REMAIN_MONEY, nbt.getInt(ItemTicket.REMAIN_MONEY) - (station.zone - nbt.getInt(ItemTicket.ENTERED_ZONE)));
//                    nbt.remove(ItemTicket.ENTERED_ZONE);
//                }
//            } else {
//                if (nbt.getBoolean(ItemTicket.ENTERED)) {
//                    player.sendMessage(Text.translatable("metropolis.info.ticket_error"), true);
//                    world.playSound(null, pos, SoundEvents.TICKET_BARRIER, SoundCategory.BLOCKS, 1.0f, 1.0f);
//                    return ActionResult.SUCCESS;
//                }
//
//                nbt.putBoolean(ItemTicket.ENTERED, true);
//                nbt.putInt(ItemTicket.ENTERED_ZONE, station.zone);
//            }
//            world.setBlockState(pos, state.with(OPEN, true));
//            world.createAndScheduleBlockTick(pos, this, 20);
//            world.playSound(null, pos, SoundEvents.TICKET_BARRIER_CONCESSIONARY, SoundCategory.BLOCKS, 1.0f, 1.0f);
//        }
//
//        return ActionResult.SUCCESS;
    }

//    @Override
//    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        world.setBlockState(pos, state.with(OPEN, false));
//    }

    public VoxelShape getOutlineShape(BlockState state, BlockView blockGetter, BlockPos pos, ShapeContext collisionContext) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        return VoxelShapes.combine(
                IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 1.0, 15.0, 16.0, facing),
                IBlock.getVoxelShapeByDirection(12.0, 0.0, 0.0, 16.0 , 15.0, 16.0, facing),
                BooleanBiFunction.OR);
    }

    public VoxelShape getCollisionShape(BlockState state, BlockView blockGetter, BlockPos blockPos, ShapeContext collisionContext) {
        Direction facing = IBlock.getStatePropertySafe(state, FACING);
        VoxelShape base = VoxelShapes.combine(
                IBlock.getVoxelShapeByDirection(0.0, 0.0, 0.0, 1.0, 24.0, 16.0, facing),
                IBlock.getVoxelShapeByDirection(15.0, 0.0, 0.0, 16.0, 24.0, 16.0, facing),
                BooleanBiFunction.OR);
        return IBlock.getStatePropertySafe(state, OPEN) ? base : VoxelShapes.union(IBlock.getVoxelShapeByDirection(0.0, 0.0, 7.0, 16.0, 24.0, 9.0, facing), base);
    }

    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite()).with(OPEN, false);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }
}
