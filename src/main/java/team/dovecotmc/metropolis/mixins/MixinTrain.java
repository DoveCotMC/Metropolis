package team.dovecotmc.metropolis.mixins;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPlatform;
import mtr.data.RailwayData;
import mtr.data.Train;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.metropolis.block.interfaces.IBlockPlatform;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(Train.class)
public abstract class MixinTrain {
    @Invoker(value = "skipScanBlocks", remap = false)
    public abstract boolean invokeSkipScanBlocks(Level var1, double var2, double var4, double var6);

    @Invoker(value = "openDoors", remap = false)
    public abstract boolean invokeOpenDoors(Level var1, Block var2, BlockPos var3, int var4);

    @Inject(at = @At("RETURN"), method = "scanDoors", cancellable = true, remap = false)
    public void scanDoors(Level world, double trainX, double trainY, double trainZ, float checkYaw, float pitch, double halfSpacing, int dwellTicks, CallbackInfoReturnable<Boolean> cir) {
        if (this.invokeSkipScanBlocks(world, trainX, trainY, trainZ)) {
            cir.setReturnValue(false);
        } else {
            boolean hasPlatform = false;
            Vec3 offsetVec = (new Vec3(1.0, 0.0, 0.0)).yRot(checkYaw).xRot(pitch);
            Vec3 traverseVec = (new Vec3(0.0, 0.0, 1.0)).yRot(checkYaw).xRot(pitch);

            for(int checkX = 1; checkX <= 3; ++checkX) {
                for(int checkY = -2; checkY <= 3; ++checkY) {
                    for(double checkZ = -halfSpacing; checkZ <= halfSpacing; ++checkZ) {
                        BlockPos checkPos = RailwayData.newBlockPos(trainX + offsetVec.x * (double)checkX + traverseVec.x * checkZ, trainY + (double)checkY, trainZ + offsetVec.z * (double)checkX + traverseVec.z * checkZ);
                        Block block = world.getBlockState(checkPos).getBlock();
                        if (block instanceof BlockPlatform || block instanceof BlockPSDAPGBase || block instanceof IBlockPlatform) {
                            if (this.invokeOpenDoors(world, block, checkPos, dwellTicks)) {
                                cir.setReturnValue(true);
                                return;
                            }

                            hasPlatform = true;
                        }
                    }
                }
            }

            cir.setReturnValue(hasPlatform);
        }
    }
}
