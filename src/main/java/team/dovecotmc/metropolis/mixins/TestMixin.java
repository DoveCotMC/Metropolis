package team.dovecotmc.metropolis.mixins;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPSDDoor;
import mtr.block.BlockTrainAnnouncer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */

@Mixin(BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase.class)
public class TestMixin {
    @Inject(method = "setOpen", at = @At("HEAD"), remap = false)
    private void test(int _open, CallbackInfo ci) {
//        try {
//            throw new IOException("1145141919810");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
