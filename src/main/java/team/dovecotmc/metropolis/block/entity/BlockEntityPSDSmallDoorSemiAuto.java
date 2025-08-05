package team.dovecotmc.metropolis.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityPSDSmallDoorSemiAuto extends BlockEntity {
    public static final String KEY_OPEN = "open";
    public float open = 0;
    public static final String KEY_ANIMATION_START_TIME = "animation_start_time";
    public long animationStartTime = 0L;

    public BlockEntityPSDSmallDoorSemiAuto(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.PSD_SMALL_DOOR, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        open = nbt.getFloat(KEY_OPEN);
        animationStartTime = nbt.getLong(KEY_ANIMATION_START_TIME);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.putFloat(KEY_OPEN, open);
        nbt.putLong(KEY_ANIMATION_START_TIME, animationStartTime);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
