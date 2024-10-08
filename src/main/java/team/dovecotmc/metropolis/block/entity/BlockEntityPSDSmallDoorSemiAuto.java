package team.dovecotmc.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityPSDSmallDoorSemiAuto extends BlockEntity {
    public static final String KEY_OPEN = "open";
    public boolean open = false;
    public static final String KEY_ANIMATION_START_TIME = "animation_start_time";
    public long animationStartTime = 0L;

    public BlockEntityPSDSmallDoorSemiAuto(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        open = nbt.getBoolean(KEY_OPEN);
        animationStartTime = nbt.getLong(KEY_ANIMATION_START_TIME);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putBoolean(KEY_OPEN, open);
        nbt.putLong(KEY_ANIMATION_START_TIME, animationStartTime);
    }
}
