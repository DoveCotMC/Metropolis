package team.dovecotmc.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityTicketMachine extends BlockEntity implements BlockTicketMachineInventory {
    public static final String TAG_TICKET_SLOT_OCCUPIED = "ticket_slot_occupied";
    public boolean ticketSlotOccupied = false;
    public static final String TAG_SELLING_TICKET_MODE = "ticket_selling_mode";
    public boolean ticketSellingMode = false;
    public static final String TAG_EMERALD_CACHE = "emerald_cache";
    public int emeraldCache = 0;
    public static final String TAG_CARD_SLOT_OCCUPIED = "card_slot_occupied";
    public boolean cardSlotOccupied = false;
    public static final String TAG_TICKET_SLOT_ANIMATION_TICK = "ticket_slot_animation_tick";
    public long ticketSlotAnimationTick = 0;
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);

    public BlockEntityTicketMachine(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.TICKET_MACHINE_BLOCK_ENTITY, pos, state);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.cardSlotOccupied = nbt.getBoolean(TAG_CARD_SLOT_OCCUPIED);
        this.ticketSellingMode = nbt.getBoolean(TAG_SELLING_TICKET_MODE);
        this.ticketSlotOccupied = nbt.getBoolean(TAG_TICKET_SLOT_OCCUPIED);
        this.emeraldCache = nbt.getInt(TAG_EMERALD_CACHE);
        this.ticketSlotAnimationTick = nbt.getLong(TAG_TICKET_SLOT_ANIMATION_TICK);

        Inventories.readNbt(nbt, items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putBoolean(TAG_CARD_SLOT_OCCUPIED, cardSlotOccupied);
        nbt.putBoolean(TAG_SELLING_TICKET_MODE, ticketSellingMode);
        nbt.putBoolean(TAG_TICKET_SLOT_OCCUPIED, ticketSlotOccupied);
        nbt.putInt(TAG_EMERALD_CACHE, emeraldCache);
        nbt.putLong(TAG_TICKET_SLOT_ANIMATION_TICK, ticketSlotAnimationTick);

        Inventories.writeNbt(nbt, items);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        BlockTicketMachineInventory.super.setStack(slot, stack);
    }
}
