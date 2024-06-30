package team.dovecotmc.metropolis.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemBlockShowStationHUD extends BlockItem implements IItemShowStationHUD {
    public ItemBlockShowStationHUD(Block block, Settings settings) {
        super(block, settings);
    }
}
