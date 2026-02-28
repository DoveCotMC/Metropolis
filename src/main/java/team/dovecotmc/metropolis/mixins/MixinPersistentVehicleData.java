package team.dovecotmc.metropolis.mixins;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */

import org.mtr.mod.data.PersistentVehicleData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PersistentVehicleData.class)
public interface MixinPersistentVehicleData {
    @Accessor(remap = false)
    double getDoorValue();
}