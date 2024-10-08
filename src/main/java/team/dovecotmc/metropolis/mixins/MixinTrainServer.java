package team.dovecotmc.metropolis.mixins;

import mtr.data.TrainServer;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatformDoor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */

@Mixin(TrainServer.class)
public class MixinTrainServer {
    @Inject(method = "openDoors(Lnet/minecraft/world/World;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;I)Z", at = @At("HEAD"), remap = false)
    private void openDoorsHead(World world, Block block, BlockPos checkPos, int dwellTicks, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof IBlockPlatformDoor) {
            ((IBlockPlatformDoor) block).setOpenState(true);
            world.createAndScheduleBlockTick(checkPos, block, dwellTicks);
        }
    }
}
