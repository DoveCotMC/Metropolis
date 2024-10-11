package team.dovecotmc.metropolis.mixins;

import mtr.data.*;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatformDoor;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */

@Mixin(TrainServer.class)
public abstract class MixinTrainServer {

    @Inject(method = "openDoors(Lnet/minecraft/world/World;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;I)Z", at = @At("HEAD"), remap = false)
    private void openDoorsHead(World world, Block block, BlockPos checkPos, int dwellTicks, CallbackInfoReturnable<Boolean> cir) {
        if (block instanceof IBlockPlatformDoor) {
            float doorOpenValue = ((MixinTrainAccessor) this).getDoorValue();
            if (doorOpenValue > 0.0f) {
                ((IBlockPlatformDoor) block).setOpenState(true, doorOpenValue, world, checkPos, world.getBlockState(checkPos));
                world.createAndScheduleBlockTick(checkPos, block, dwellTicks);
            } else {
                ((IBlockPlatformDoor) block).setOpenState(false, doorOpenValue, world, checkPos, world.getBlockState(checkPos));
            }
        }
    }
}
