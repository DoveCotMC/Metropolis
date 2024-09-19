package team.dovecotmc.metropolis.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityCamera extends BlockEntity {
    public BlockEntityCamera(BlockPos pos, BlockState state) {
        super(MetroBlockEntities.CAMERA_BLOCK_ENTITY, pos, state);
    }
}
