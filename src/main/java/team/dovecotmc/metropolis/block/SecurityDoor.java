package team.dovecotmc.metropolis.block;

import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.DyeColor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class SecurityDoor extends Block {
    public SecurityDoor() {
        super(Settings.of(Material.METAL, DyeColor.LIGHT_GRAY));
    }
}
