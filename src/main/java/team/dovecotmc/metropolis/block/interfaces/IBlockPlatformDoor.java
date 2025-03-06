package team.dovecotmc.metropolis.block.interfaces;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public interface IBlockPlatformDoor {
    void setOpenState(boolean open, float openValue, World world, BlockPos pos, BlockState state);
}
