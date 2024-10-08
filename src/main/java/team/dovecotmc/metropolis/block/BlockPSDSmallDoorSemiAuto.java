package team.dovecotmc.metropolis.block;

import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatformDoor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockPSDSmallDoorSemiAuto extends BlockHorizontalFacing implements IBlockPlatform, IBlockPlatformDoor {
    public BlockPSDSmallDoorSemiAuto(Settings settings) {
        super(settings.nonOpaque());
    }

    @Override
    public void setOpenState(boolean state) {
    }
}
