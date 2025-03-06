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
public class BlockEntityFareAdj extends BlockEntity implements BlockFareAdjInventory {
    public static final String TICKET_ANIMATION_BEGIN_TIME = "ticket_animation_begin_time";
    public long ticket_animation_begin_time = 0;
    public static final String CARD_ANIMATION_IN_BEGIN_TIME = "card_animation_in_begin_time";
    public long card_animation_in_begin_time = 0;
    public static final String CARD_ANIMATION_OUT_BEGIN_TIME = "card_animation_out_begin_time";
    public long card_animation_out_begin_time = 0;

    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(1, ItemStack.EMPTY);

    public BlockEntityFareAdj(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.FARE_ADJ_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.ticket_animation_begin_time = nbt.getLong(TICKET_ANIMATION_BEGIN_TIME);
        this.card_animation_in_begin_time = nbt.getLong(CARD_ANIMATION_IN_BEGIN_TIME);
        this.card_animation_out_begin_time = nbt.getLong(CARD_ANIMATION_OUT_BEGIN_TIME);

        Inventories.readNbt(nbt, items);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putLong(TICKET_ANIMATION_BEGIN_TIME, ticket_animation_begin_time);
        nbt.putLong(CARD_ANIMATION_IN_BEGIN_TIME, card_animation_in_begin_time);
        nbt.putLong(CARD_ANIMATION_OUT_BEGIN_TIME, card_animation_out_begin_time);

        Inventories.writeNbt(nbt, items);
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
    public DefaultedList<ItemStack> getItems() {
        return items;
    }
}
