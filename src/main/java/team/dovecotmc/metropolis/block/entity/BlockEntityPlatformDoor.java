package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockEntityPlatformDoor extends BlockEntity {
    public static final int ANIMATION_TIME = 20;

    private @Nullable BlockPos controllerPos = null;
    private long lastToggleTime = 0;

    public BlockEntityPlatformDoor(BlockPos blockPos, BlockState blockState) {
        super(MetroBlockEntities.PLATFORM_DOOR_BLOCK_ENTITY, blockPos, blockState);
    }

    public float getOpenValue(Level level, float f) {
        if (controllerPos == null)
            return 1f;

        return Mth.clamp((level.getGameTime() - lastToggleTime + f) / ANIMATION_TIME, 0f, 1f);
    }

    public void setBindingBlock(BlockPos blockPos) {
        controllerPos = blockPos;
    }

    public void setLastToggleTime(long time) {
        lastToggleTime = time;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);

        if (compoundTag.contains("controller_pos", CompoundTag.TAG_LONG))
            controllerPos = BlockPos.of(compoundTag.getLong("controller_pos"));

        lastToggleTime = compoundTag.getLong("last_toggle_time");
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);

        if (controllerPos != null)
            compoundTag.putLong("controller_pos", controllerPos.asLong());

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
