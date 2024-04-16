package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.BlockDirectionalDoubleBlockBase;
import mtr.block.IBlock;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityTicketMachine;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.metropolis.item.MetroItems;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTicketMachine extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider {
    public BlockTicketMachine() {
        super(Settings.of(Material.METAL).nonOpaque().luminance(value -> 1).requiresTool());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.PASS;
        }

        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        if (state.get(IBlock.HALF) == DoubleBlockHalf.LOWER) {
            return ActionResult.PASS;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        BlockEntityTicketMachine entity = (BlockEntityTicketMachine) world.getBlockEntity(pos);

        NbtCompound nbt = entity.createNbt().copy();

        ItemStack itemStack = player.getStackInHand(hand);
//        System.out.println(nbt.getBoolean(BlockEntityTicketMachine.TAG_SELLING_TICKET_MODE));
//        System.out.println(nbt.getBoolean(BlockEntityTicketMachine.TAG_TICKET_SLOT_OCCUPIED));
//        if (nbt.getBoolean(BlockEntityTicketMachine.TAG_SELLING_TICKET_MODE)) {
//            nbt.putBoolean(BlockEntityTicketMachine.TAG_TICKET_SLOT_OCCUPIED, true);
//            nbt.putBoolean(BlockEntityTicketMachine.TAG_SELLING_TICKET_MODE, false);
//            System.out.println(111);
//            entity.readNbt(nbt);
//        } else if (nbt.getBoolean(BlockEntityTicketMachine.TAG_TICKET_SLOT_OCCUPIED)) {
//            System.out.println(111);
//            player.giveItemStack(entity.getStack(1));
//            nbt.putBoolean(BlockEntityTicketMachine.TAG_TICKET_SLOT_OCCUPIED, false);
//            entity.readNbt(nbt);
//            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
//        } else if (itemStack.getItem() instanceof ItemTicket) {
//            if (!((ItemTicket) itemStack.getItem()).disposable) {
//                if (nbt.getBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED)) {
//                    player.setStackInHand(hand, entity.getStack(0));
//                    nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, true);
//                    entity.readNbt(nbt);
//                } else {
//                    player.setStackInHand(hand, ItemStack.EMPTY);
//                    nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, true);
//                    entity.readNbt(nbt);
//                }
//                entity.setStack(0, itemStack);
//                world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
//            }
//        } else if (player.getStackInHand(hand).getItem().equals(Items.EMERALD)) {
//            if (nbt.getBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED)) {
//                NbtCompound nbtCard = entity.getStack(0).getOrCreateNbt();
//                nbtCard.putInt(ItemTicket.REMAIN_MONEY, nbtCard.getInt(ItemTicket.REMAIN_MONEY) + itemStack.getCount());
//                player.setStackInHand(hand, ItemStack.EMPTY);
//                world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.BLOCKS, 1f, 1f);
//            } else {
//                ItemStack itemStack1 = itemStack;
//                itemStack1.setCount(itemStack.getCount() - 1);
//                player.setStackInHand(hand, itemStack1);
////                nbt.putInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE, nbt.getInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE) + 1);
//                world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.BLOCKS, 1f, 1f);
//                nbt.putBoolean(BlockEntityTicketMachine.TAG_SELLING_TICKET_MODE, true);
//                if (entity.getStack(1).isEmpty()) {
//                    // TODO: Configable
//                    ItemStack ticketStack = new ItemStack(MetroItems.ITEM_TICKET);
//                    NbtCompound ticketNbt = ticketStack.getOrCreateNbt();
//                    ticketNbt.putInt(ItemTicket.REMAIN_MONEY, 1);
//                    System.out.println(nbt);
//                    entity.readNbt(nbt);
//                    entity.setStack(1, ticketStack);
//                } else {
//                    ItemStack ticketStack = entity.getStack(1);
//                    NbtCompound ticketNbt = ticketStack.getOrCreateNbt();
//                    ticketNbt.putInt(ItemTicket.REMAIN_MONEY, ticketNbt.getInt(ItemTicket.REMAIN_MONEY) + 1);
//                    System.out.println(nbt);
//                    entity.readNbt(nbt);
//                    entity.setStack(1, ticketStack);
//                }
//            }
//        } else {
//            if (nbt.getBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED)) {
//                player.giveItemStack(entity.getStack(0));
//                nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, false);
//                entity.readNbt(nbt);
//                world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
//            }
//        }

        // Scheduled item updater tasks
        List<Pair<Integer, ItemStack>> scheduledItemUpdaterTasks = new ArrayList<>();

        // Ticket or Card mode
        if (entity.ticketSlotOccupied) {
            nbt.putBoolean(BlockEntityTicketMachine.TAG_TICKET_SLOT_OCCUPIED, false);
            nbt.putInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE, 0);
            player.giveItemStack(entity.getStack(1));
            world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
        } else {
            // Check the item and switch the mode
            if (itemStack.getItem().equals(Items.EMERALD)) {
                // Charge mode
                // Check the mode of the ticket machine
                if (entity.cardSlotOccupied) {
                    ItemStack cardStack = entity.getStack(0);
                    NbtCompound cardNbt = cardStack.getOrCreateNbt();
                    cardNbt.putInt(ItemTicket.REMAIN_MONEY, cardNbt.getInt(ItemTicket.REMAIN_MONEY) + 1);
                    itemStack.setCount(itemStack.getCount() - 1);
                    player.setStackInHand(hand, itemStack);
                    scheduledItemUpdaterTasks.add(new Pair<>(0, cardStack));
                    world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.BLOCKS, 1f, 1f);
                } else {
                    // Enable selling mode
                    itemStack.setCount(itemStack.getCount() - 1);
                    player.setStackInHand(hand, itemStack);
                    nbt.putInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE, nbt.getInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE) + 1);
                    nbt.putBoolean(BlockEntityTicketMachine.TAG_SELLING_TICKET_MODE, true);
                    world.playSound(null, pos, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, SoundCategory.BLOCKS, 1f, 1f);
                }
            } else if (itemStack.getItem() instanceof ItemTicket) {
                if (!entity.ticketSellingMode && !((ItemTicket) itemStack.getItem()).disposable) {
                    scheduledItemUpdaterTasks.add(new Pair<>(0, itemStack));
                    nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, true);
                    player.setStackInHand(hand, ItemStack.EMPTY);
                    nbt.putLong(BlockEntityTicketMachine.TAG_TICKET_SLOT_ANIMATION_TICK, world.getTime());
                }
            } else {
                // Take the ticket away or print the ticket
                if (entity.ticketSellingMode) {
                    ItemStack ticketStack = new ItemStack(MetroItems.ITEM_TICKET);
                    NbtCompound ticketNbt = ticketStack.getOrCreateNbt();
                    ticketNbt.putInt(ItemTicket.REMAIN_MONEY, nbt.getInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE));
                    scheduledItemUpdaterTasks.add(new Pair<>(1, ticketStack));

                    nbt.putBoolean(BlockEntityTicketMachine.TAG_TICKET_SLOT_OCCUPIED, true);
                    nbt.putBoolean(BlockEntityTicketMachine.TAG_SELLING_TICKET_MODE, false);
                    nbt.putLong(BlockEntityTicketMachine.TAG_TICKET_SLOT_ANIMATION_TICK, world.getTime());
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                } else if (entity.cardSlotOccupied) {
                    nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, false);
                    nbt.putInt(BlockEntityTicketMachine.TAG_EMERALD_CACHE, 0);
                    player.giveItemStack(entity.getStack(0));
                    world.playSound(null, pos, SoundEvents.BLOCK_WOOL_BREAK, SoundCategory.BLOCKS, 1f, 1f);
                }
            }
        }

        // Sync data
        entity.readNbt(nbt);
        for (Pair<Integer, ItemStack> object : scheduledItemUpdaterTasks) {
            entity.setStack(object.getLeft(), object.getRight());
        }
        serverPlayer.networkHandler.sendPacket(entity.toUpdatePacket());

        return ActionResult.SUCCESS;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTicketMachine(pos, state);
    }

    // Copied from BlockWithEntity
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity != null && blockEntity.onSyncedBlockEvent(type, data);
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
    }
}
