package team.dovecotmc.metropolis.block;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.material.MapColor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockMetroPIDSOne extends HorizontalDirectionalBlock {
    public BlockMetroPIDSOne() {
        super(Properties.of()
                .mapColor(MapColor.METAL)
                .noOcclusion());
    }
}
