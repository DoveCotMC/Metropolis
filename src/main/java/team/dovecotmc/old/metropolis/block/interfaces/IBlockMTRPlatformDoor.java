package team.dovecotmc.old.metropolis.block.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public interface IBlockMTRPlatformDoor extends IBlockPlatform {
    void setOpenState(boolean open, float doorValue, Level world, BlockPos pos, BlockState state);
}
