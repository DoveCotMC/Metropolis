package team.dovecotmc.metropolis.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface IBlockPlatformDoor {
    void setOpenState(boolean open, float openValue, Level world, BlockPos pos, BlockState state);
}
