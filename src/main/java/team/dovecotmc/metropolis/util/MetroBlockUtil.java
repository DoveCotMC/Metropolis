package team.dovecotmc.metropolis.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockUtil {
    public static VoxelShape getVoxelShapeByDirection(double x1, double y1, double z1, double x2, double y2, double z2, Direction facing) {
        return switch (facing) {
            case NORTH -> Block.createCuboidShape(x1, y1, z1, x2, y2, z2);
            case EAST -> Block.createCuboidShape(16 - z2, y1, x1, 16 - z1, y2, x2);
            case SOUTH -> Block.createCuboidShape(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
            case WEST -> Block.createCuboidShape(z1, y1, 16 - x2, z2, y2, 16 - x1);
            default -> VoxelShapes.fullCube();
        };
    }
}
