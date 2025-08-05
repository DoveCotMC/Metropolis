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
public class BlockEntitySecurityInspectionMachine extends BlockEntity implements BlockSecurityInspectionMachineInventory {
    private final NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);

    public static final String ITEM_ANIMATION_TIME = "item_animation_time";
    public long itemAnimationTime;

    public BlockEntitySecurityInspectionMachine(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.SECURITY_INSPECTION_MACHINE_BLOCK_ENTITY, pos, state);
        this.itemAnimationTime = 0;
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
        this.itemAnimationTime = nbt.getLong(ITEM_ANIMATION_TIME);
        ContainerHelper.loadAllItems(nbt, items);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putLong(ITEM_ANIMATION_TIME, this.itemAnimationTime);
        ContainerHelper.saveAllItems(nbt, items);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        BlockSecurityInspectionMachineInventory.super.setItem(slot, stack);
    }
}
