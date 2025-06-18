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
public class BlockEntityFareAdj extends BlockEntity implements BlockFareAdjInventory {
    public static final String TICKET_ANIMATION_BEGIN_TIME = "ticket_animation_begin_time";
    public long ticket_animation_begin_time = 0;
    public static final String CARD_ANIMATION_IN_BEGIN_TIME = "card_animation_in_begin_time";
    public long card_animation_in_begin_time = 0;
    public static final String CARD_ANIMATION_OUT_BEGIN_TIME = "card_animation_out_begin_time";
    public long card_animation_out_begin_time = 0;

    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public BlockEntityFareAdj(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.FARE_ADJ_BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.ticket_animation_begin_time = nbt.getLong(TICKET_ANIMATION_BEGIN_TIME);
        this.card_animation_in_begin_time = nbt.getLong(CARD_ANIMATION_IN_BEGIN_TIME);
        this.card_animation_out_begin_time = nbt.getLong(CARD_ANIMATION_OUT_BEGIN_TIME);

        ContainerHelper.loadAllItems(nbt, items);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.putLong(TICKET_ANIMATION_BEGIN_TIME, ticket_animation_begin_time);
        nbt.putLong(CARD_ANIMATION_IN_BEGIN_TIME, card_animation_in_begin_time);
        nbt.putLong(CARD_ANIMATION_OUT_BEGIN_TIME, card_animation_out_begin_time);

        ContainerHelper.saveAllItems(nbt, items);
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
    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
