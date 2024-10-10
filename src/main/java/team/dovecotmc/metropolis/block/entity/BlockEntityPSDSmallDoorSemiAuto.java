package team.dovecotmc.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
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
    public void readNbt(NbtCompound nbt) {
        open = nbt.getFloat(KEY_OPEN);
        animationStartTime = nbt.getLong(KEY_ANIMATION_START_TIME);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        nbt.putFloat(KEY_OPEN, open);
        nbt.putLong(KEY_ANIMATION_START_TIME, animationStartTime);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
}
