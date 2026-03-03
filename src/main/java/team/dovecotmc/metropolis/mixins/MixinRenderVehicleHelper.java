package team.dovecotmc.metropolis.mixins;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Init;
import org.mtr.mod.render.PositionAndRotation;
import org.mtr.mod.render.RenderVehicleHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.dovecotmc.old.metropolis.block.interfaces.IBlockMTRPlatform;
import team.dovecotmc.old.metropolis.block.interfaces.IBlockMTRPlatformDoor;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(RenderVehicleHelper.class)
public abstract class MixinRenderVehicleHelper {
    @Shadow(remap = false)
    @Final
    private static int CHECK_DOOR_RADIUS_XZ;

    @Shadow(remap = false)
    @Final
    private static int CHECK_DOOR_RADIUS_Y;

    @Inject(
            method = "canOpenDoors",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private static void met$InjectPlatformDetection(Box doorway, PositionAndRotation positionAndRotation, double doorValue, CallbackInfoReturnable<Boolean> cir) {
        final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
        if (clientWorld == null) {
            return;
        }

        final Vector3d doorwayPosition1 = positionAndRotation.transformForwards(new Vector3d(doorway.getMinXMapped(), doorway.getMaxYMapped(), doorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
        final Vector3d doorwayPosition2 = positionAndRotation.transformForwards(new Vector3d(doorway.getMaxXMapped(), doorway.getMaxYMapped(), doorway.getMinZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
        final Vector3d doorwayPosition3 = positionAndRotation.transformForwards(new Vector3d(doorway.getMaxXMapped(), doorway.getMaxYMapped(), doorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
        final Vector3d doorwayPosition4 = positionAndRotation.transformForwards(new Vector3d(doorway.getMinXMapped(), doorway.getMaxYMapped(), doorway.getMaxZMapped()), Vector3d::rotateX, Vector3d::rotateY, Vector3d::add);
        final double minX = Math.min(Math.min(doorwayPosition1.getXMapped(), doorwayPosition2.getXMapped()), Math.min(doorwayPosition3.getXMapped(), doorwayPosition4.getXMapped()));
        final double maxX = Math.max(Math.max(doorwayPosition1.getXMapped(), doorwayPosition2.getXMapped()), Math.max(doorwayPosition3.getXMapped(), doorwayPosition4.getXMapped()));
        final double minY = Math.min(Math.min(doorwayPosition1.getYMapped(), doorwayPosition2.getYMapped()), Math.min(doorwayPosition3.getYMapped(), doorwayPosition4.getYMapped()));
        final double maxY = Math.max(Math.max(doorwayPosition1.getYMapped(), doorwayPosition2.getYMapped()), Math.max(doorwayPosition3.getYMapped(), doorwayPosition4.getYMapped()));
        final double minZ = Math.min(Math.min(doorwayPosition1.getZMapped(), doorwayPosition2.getZMapped()), Math.min(doorwayPosition3.getZMapped(), doorwayPosition4.getZMapped()));
        final double maxZ = Math.max(Math.max(doorwayPosition1.getZMapped(), doorwayPosition2.getZMapped()), Math.max(doorwayPosition3.getZMapped(), doorwayPosition4.getZMapped()));
        boolean canOpenDoors = false;


        for (double checkX = minX - CHECK_DOOR_RADIUS_XZ; checkX <= maxX + CHECK_DOOR_RADIUS_XZ; checkX++) {
            for (double checkY = minY - CHECK_DOOR_RADIUS_Y; checkY <= maxY + CHECK_DOOR_RADIUS_Y; checkY++) {
                for (double checkZ = minZ - CHECK_DOOR_RADIUS_XZ; checkZ <= maxZ + CHECK_DOOR_RADIUS_XZ; checkZ++) {
                    final BlockPos checkPos = Init.newBlockPos(checkX, checkY, checkZ);
                    final BlockState blockState = clientWorld.getBlockState(checkPos);
                    final Block block = blockState.getBlock();
                    if (block.data instanceof IBlockMTRPlatform) {
                        canOpenDoors = true;

                        if (block.data instanceof IBlockMTRPlatformDoor door) {
                            door.setOpenState(
                                    false,
                                    (float) doorValue,
                                    clientWorld.data,
                                    checkPos.data,
                                    blockState.data
                            );
                        }
                    }
                }
            }
        }

//        for (double checkX = minX - CHECK_DOOR_RADIUS_XZ; checkX <= maxX + CHECK_DOOR_RADIUS_XZ; checkX++) {
//            for (double checkY = minY - CHECK_DOOR_RADIUS_Y; checkY <= maxY + CHECK_DOOR_RADIUS_Y; checkY++) {
//                for (double checkZ = minZ - CHECK_DOOR_RADIUS_XZ; checkZ <= maxZ + CHECK_DOOR_RADIUS_XZ; checkZ++) {
//                    final BlockPos checkPos = Init.newBlockPos(checkX, checkY, checkZ);
//                    final BlockState blockState = clientWorld.getBlockState(checkPos);
//                    final Block block = blockState.getBlock();
//                    if (block.data instanceof PlatformHelper) {
//                        canOpenDoors = true;
//                    }
//                }
//            }
//        }

        cir.setReturnValue(canOpenDoors || cir.getReturnValue());
    }
}
