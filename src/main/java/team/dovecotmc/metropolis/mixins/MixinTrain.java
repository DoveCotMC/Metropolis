package team.dovecotmc.metropolis.mixins;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPlatform;
import mtr.data.RailwayData;
import mtr.data.Train;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.metropolis.IBlockPlatform;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(Train.class)
public abstract class MixinTrain {
    @Invoker(value = "skipScanBlocks", remap = false)
    public abstract boolean invokeSkipScanBlocks(World var1, double var2, double var4, double var6);

    @Invoker(value = "openDoors", remap = false)
    public abstract boolean invokeOpenDoors(World var1, Block var2, BlockPos var3, int var4);

    @Inject(at = @At("RETURN"), method = "scanDoors", cancellable = true, remap = false)
    public void scanDoors(World world, double trainX, double trainY, double trainZ, float checkYaw, float pitch, double halfSpacing, int dwellTicks, CallbackInfoReturnable<Boolean> cir) {
        if (this.invokeSkipScanBlocks(world, trainX, trainY, trainZ)) {
            cir.setReturnValue(false);
        } else {
            boolean hasPlatform = false;
            Vec3d offsetVec = (new Vec3d(1.0, 0.0, 0.0)).rotateY(checkYaw).rotateX(pitch);
            Vec3d traverseVec = (new Vec3d(0.0, 0.0, 1.0)).rotateY(checkYaw).rotateX(pitch);

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
