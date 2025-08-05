package team.dovecotmc.metropolis.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemBlockShowStationHUD extends BlockItem implements IItemShowStationHUD {
    public ItemBlockShowStationHUD(Block block, Properties settings) {
        super(block, settings);
    }
}
