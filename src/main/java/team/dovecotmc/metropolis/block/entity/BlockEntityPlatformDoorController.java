package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityPlatformDoorController extends BlockEntity {
    public boolean openState = false;
    public long lastToggleTime = 0;

    public BlockEntityPlatformDoorController(BlockPos blockPos, BlockState blockState) {
        super(MetroBlockEntities.PLATFORM_DOOR_CONTROLLER_BLOCK_ENTITY, blockPos, blockState);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        openState = compoundTag.getBoolean("open_state");
        lastToggleTime = compoundTag.getLong("last_toggle_time");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putBoolean("open_state", openState);
        compoundTag.putLong("last_toggle_time", lastToggleTime);
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
}
