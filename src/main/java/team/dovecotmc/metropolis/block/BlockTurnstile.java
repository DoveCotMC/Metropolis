package team.dovecotmc.metropolis.block;

import mtr.block.BlockTicketBarrier;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.item.IItemOpenGate;
import team.dovecotmc.metropolis.item.ItemCard;
import team.dovecotmc.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.network.MetroServerNetwork;
import team.dovecotmc.metropolis.util.MetroBlockUtil;
import team.dovecotmc.metropolis.util.MtrCommonUtil;
import team.dovecotmc.metropolis.util.MtrSoundUtil;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("deprecation")
public class BlockTurnstile extends HorizontalDirectionalBlock implements EntityBlock, IBlockStationOverlayShouldRender {
    public static final int CLOSE_DELAY = 80;
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    public static final IntegerProperty TYPE = IntegerProperty.create("type", 0, 2);
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    public final boolean icOnly;

    public BlockTurnstile(boolean icOnly) {
        super(Properties.of(Material.METAL).strength(4.0f).noOcclusion());
        this.icOnly = icOnly;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (world.getBlockEntity(pos) instanceof BlockEntityTurnstile blockEntity && !state.getValue(OPEN)) {
            Station station = MtrStationUtil.getStationByPos(pos, world);
            if (station == null) {
                player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.turnstile_error"), true);
                return InteractionResult.SUCCESS;
            }

            ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
            CompoundTag nbt = blockEntity.saveWithoutMetadata();

            BlockEntityTurnstile.EnumTurnstileType type = BlockEntityTurnstile.EnumTurnstileType.get(state.getValue(TYPE));
            if (stack.getItem() == MtrCommonUtil.getBrushItem()) {
                if (type != BlockEntityTurnstile.EnumTurnstileType.ENTER && blockEntity.getItems().isEmpty()) {
                    player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.unable_switch_turnstile_type"), true);
                    return InteractionResult.SUCCESS;
                }

                world.playSound(null, pos, SoundEvents.COPPER_BREAK, SoundSource.BLOCKS, 1f, 1f);
                int nextTypeIndex = (type.index + 1) % 2;
                world.setBlockAndUpdate(pos, state.setValue(TYPE, nextTypeIndex));
                Component typeName = MALocalizationUtil.translatableText("misc.metropolis.turnstile_mode." + BlockEntityTurnstile.EnumTurnstileType.get(nextTypeIndex).name().toLowerCase());
                player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.turnstile_type", typeName), true);
                blockEntity.load(nbt);
                blockEntity.clearContent();
                ((ServerPlayer) player).connection.send(blockEntity.getUpdatePacket());
                for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                    MetroServerNetwork.removeInventoryItem(i, pos, (ServerPlayer) player);
                }

                return InteractionResult.SUCCESS;
            }

            // Open Gate
            if ((stack.getItem() instanceof ItemCard && ((ItemCard) stack.getItem()).infiniteBalance)) {
                if (player.isCreative() && world.getGameTime() - nbt.getLong(BlockEntityTurnstile.TICKET_ANIMATION_START) >= 7) {
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundSource.BLOCKS, 1f, 1f);
                    world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1f, 1f);
                    player.addItem(blockEntity.getItem(0));
                    blockEntity.removeItemNoUpdate(0);

                    ((ServerPlayer) player).connection.send(blockEntity.getUpdatePacket());
                    MetroServerNetwork.removeInventoryItem(0, pos, (ServerPlayer) player);

                    // Open
                    world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
                    world.scheduleTick(pos, this, CLOSE_DELAY);
                    return InteractionResult.SUCCESS;
                }
            }

            if (type == BlockEntityTurnstile.EnumTurnstileType.ENTER) {
                if (!blockEntity.getItem(0).isEmpty()) {
                    if (world.getGameTime() - nbt.getLong(BlockEntityTurnstile.TICKET_ANIMATION_START) >= 7) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundSource.BLOCKS, 1f, 1f);
                        world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1f, 1f);
                        player.addItem(blockEntity.getItem(0));
                        blockEntity.removeItemNoUpdate(0);

                        ((ServerPlayer) player).connection.send(blockEntity.getUpdatePacket());
                        MetroServerNetwork.removeInventoryItem(0, pos, (ServerPlayer) player);

                        // Open
                        world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
                        world.scheduleTick(pos, this, CLOSE_DELAY);
                        return InteractionResult.SUCCESS;
                    }
                }

                // Enter
                if (stack.getItem() instanceof ItemTicket) {
                    if (icOnly) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.use_other_turnstile"), true);
                        return InteractionResult.SUCCESS;
                    }

                    if (stack.getOrCreateTag().contains(ItemTicket.ENTERED_STATION) || stack.getOrCreateTag().contains(ItemTicket.ENTERED_ZONE)) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.to_service_center"), true);
                        return InteractionResult.SUCCESS;
                    }

                    world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1f, 1f);

                    CompoundTag stackNbt = stack.getOrCreateTag();
                    stackNbt.putString(ItemTicket.ENTERED_STATION, station.name);
                    stackNbt.putInt(ItemTicket.ENTERED_ZONE, station.zone);

                    ItemStack newStack = new ItemStack(MetroItems.ITEM_SINGLE_TRIP_TICKET_USED);
                    newStack.setTag(stackNbt);
                    blockEntity.setItem(0, newStack);
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.saveWithoutMetadata();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getGameTime());
                    blockEntity.load(nbt);
                } else if (stack.getItem() instanceof ItemCard) {
                    if (stack.getOrCreateTag().contains(ItemCard.ENTERED_STATION) || stack.getOrCreateTag().contains(ItemCard.ENTERED_ZONE)) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.to_service_center"), true);
                        return InteractionResult.SUCCESS;
                    }

                    // TODO: Custom Cards sound
                    CompoundTag stackNbt = stack.getOrCreateTag();
                    stackNbt.putString(ItemCard.ENTERED_STATION, station.name);
                    stackNbt.putInt(ItemCard.ENTERED_ZONE, station.zone);
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundSource.BLOCKS, 1f, 1f);

                    world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
                    world.scheduleTick(pos, this, CLOSE_DELAY);
                }
            } else if (type == BlockEntityTurnstile.EnumTurnstileType.EXIT) {
                CompoundTag stackNbt = stack.getOrCreateTag();
                if (!(stack.getItem() instanceof IItemOpenGate) && !stackNbt.contains(ItemTicket.ENTERED_STATION) && !stackNbt.contains(ItemTicket.ENTERED_ZONE)) {
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundSource.BLOCKS, 1f, 1f);
                    player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.to_service_center"), true);
                    return InteractionResult.SUCCESS;
                }

                if (stack.getItem() instanceof ItemTicket || stack.getItem() instanceof IItemOpenGate) {
                    if (icOnly) {
                        world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER, SoundSource.BLOCKS, 1f, 1f);
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.use_other_turnstile"), true);
                        return InteractionResult.SUCCESS;
                    }

                    int cost = Math.abs(station.zone - stackNbt.getInt(ItemTicket.ENTERED_ZONE)) + 1;
                    int balance = stackNbt.getInt(ItemTicket.BALANCE);

                    if (balance < cost && !(stack.getItem() instanceof IItemOpenGate)) {
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.no_enough_balance"), true);
                        return InteractionResult.SUCCESS;
                    }

                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundSource.BLOCKS, 1f, 1f);
                    world.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.BLOCKS, 1f, 1f);

                    blockEntity.setItem(0, stack);
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.saveWithoutMetadata();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getGameTime());
                    blockEntity.load(nbt);

                    world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
                    world.scheduleTick(pos, this, CLOSE_DELAY);
                } else if (stack.getItem() instanceof ItemCard) {
                    int cost = Math.abs(station.zone - stackNbt.getInt(ItemCard.ENTERED_ZONE)) + 1;
                    int balance = stackNbt.getInt(ItemCard.BALANCE);

                    if (balance < cost) {
                        player.displayClientMessage(MALocalizationUtil.translatableText("info.metropolis.no_enough_balance"), true);
                        return InteractionResult.SUCCESS;
                    }

                    // TODO: Cards sound
                    stackNbt.remove(ItemCard.ENTERED_STATION);
                    stackNbt.remove(ItemCard.ENTERED_ZONE);
                    stackNbt.putInt(ItemCard.BALANCE, balance - cost);
                    world.playSound(null, pos, MtrSoundUtil.TICKET_BARRIER_CONCESSIONARY, SoundSource.BLOCKS, 1f, 1f);

                    world.setBlockAndUpdate(pos, state.setValue(OPEN, true));
                    world.scheduleTick(pos, this, CLOSE_DELAY);
                }
            }
            // TODO: Direct debit
            /* else if (type == BlockEntityTurnstile.EnumTurnstileType.DIRECT_DEBIT) {
                NbtCompound stackNbt = stack.getOrCreateNbt();

                int cost = Math.abs(station.zone - stackNbt.getInt(ItemTicket.ENTERED_ZONE)) + 1;
                int balance = stackNbt.getInt(ItemTicket.BALANCE);

                if (balance < cost) {
                    player.sendMessage(MALocalizationUtil.translatableText("info.metropolis.no_enough_balance"), true);
                    return ActionResult.SUCCESS;
                }

                if (stack.getItem() instanceof ItemTicket) {
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                    blockEntity.setStack(0, stack);
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

                    nbt = blockEntity.createNbt();
                    nbt.putLong(BlockEntityTurnstile.TICKET_ANIMATION_START, world.getTime());
                    blockEntity.readNbt(nbt);

                    world.setBlockState(pos, state.with(OPEN, true));
                    world.createAndScheduleBlockTick(pos, this, CLOSE_DELAY);
                } else if (stack.getItem() instanceof ItemCard) {
                    // TODO: Cards
                }
            }*/

            ((ServerPlayer) player).connection.send(blockEntity.getUpdatePacket());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING).getClockWise();
        if (world.getBlockState(pos.relative(facing)).getBlock() instanceof BlockTurnstile) {
            return state.setValue(CONNECTED, true);
        } else {
            return state.setValue(CONNECTED, false);
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        Direction facing = state.getValue(FACING).getClockWise();
        if (world.getBlockState(pos.relative(facing)).getBlock() instanceof BlockTurnstile) {
            world.setBlockAndUpdate(pos, state.setValue(CONNECTED, true));
        } else {
            world.setBlockAndUpdate(pos, state.setValue(CONNECTED, false));
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlockAndUpdate(pos, state.setValue(OPEN, false));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter world, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        return Shapes.or(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, 0, 3, 16, 16, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, 0, 16, 16, 16, facing)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return Shapes.or(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, -4, 3, 17, 20, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, -4, 16, 17, 20, facing)
        );
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        VoxelShape shape = Shapes.or(
                MetroBlockUtil.getVoxelShapeByDirection(0, 0, -4, 2, 24, 20, facing),
                MetroBlockUtil.getVoxelShapeByDirection(15, 0, -4, 16, 24, 20, facing)
        );
        return state.getValue(OPEN) ? shape : Shapes.or(shape, MetroBlockUtil.getVoxelShapeByDirection(0, 0, 9, 16, 24, 11, facing));
    }

    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(OPEN, false).setValue(TYPE, 0).setValue(CONNECTED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(OPEN).add(TYPE).add(CONNECTED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTurnstile(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        BlockEntityTurnstile.EnumTurnstileType type = BlockEntityTurnstile.EnumTurnstileType.get(state.getValue(TYPE));
        switch (type) {
            case ENTER -> {
                if (this.icOnly) {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_IC_ONLY_ENTER);
                } else {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_ENTER);
                }
            }
            case EXIT -> {
                if (this.icOnly) {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_IC_ONLY_EXIT);
                } else {
                    return new ItemStack(MetroItems.ITEM_TURNSTILE_EXIT);
                }
            }
        }
        return new ItemStack(MetroItems.ITEM_TURNSTILE_ENTER);
    }

    @Override
    public boolean shouldRenderName() {
        return false;
    }
}
