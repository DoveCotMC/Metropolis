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
public class BlockEntityTurnstile extends BlockEntity implements BlockTurnstileInventory {
    public static final String TICKET_ANIMATION_START = "ticket_animation_start_time";
    public long ticketAnimationStartTime;
    public static final String TURNSTILE_TYPE = "turnstile_type";
    public EnumTurnstileType type;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public BlockEntityTurnstile(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.TURNSTILE_BLOCK_ENTITY, pos, state);
        this.ticketAnimationStartTime = 0;
        this.type = EnumTurnstileType.ENTER;
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
        this.ticketAnimationStartTime = nbt.getLong(TICKET_ANIMATION_START);
        this.type = EnumTurnstileType.get(nbt.getInt(TURNSTILE_TYPE));
        Inventories.readNbt(nbt, items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putLong(TICKET_ANIMATION_START, this.ticketAnimationStartTime);
        nbt.putInt(TURNSTILE_TYPE, this.type.index);
        Inventories.writeNbt(nbt, items);
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        BlockTurnstileInventory.super.setStack(slot, stack);
    }

    public enum EnumTurnstileType {
        ENTER(0),
        EXIT(1),
        DIRECT_DEBIT(2);

        public final int index;

        EnumTurnstileType(int index) {
            this.index = index;
        }

        public static EnumTurnstileType get(int index) {
            return switch (index) {
                case 0 -> ENTER;
                case 1 -> EXIT;
                case 2 -> DIRECT_DEBIT;
                default -> null;
            };
        }
    }
}