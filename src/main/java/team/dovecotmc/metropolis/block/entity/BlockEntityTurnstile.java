package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityTurnstile extends BlockEntity implements BlockTurnstileInventory {
    public static final String TICKET_ANIMATION_START = "ticket_animation_start_time";
    public long ticketAnimationStartTime;

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public BlockEntityTurnstile(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.TURNSTILE_BLOCK_ENTITY, pos, state);
        this.ticketAnimationStartTime = 0;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.ticketAnimationStartTime = nbt.getLong(TICKET_ANIMATION_START);
        ContainerHelper.loadAllItems(nbt, items);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putLong(TICKET_ANIMATION_START, this.ticketAnimationStartTime);
        ContainerHelper.saveAllItems(nbt, items);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        BlockTurnstileInventory.super.setItem(slot, stack);
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