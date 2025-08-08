package team.dovecotmc.metropolis.mixins;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */

//@Mixin(TrainServer.class)
public abstract class MixinTrainServer {
//    @Inject(method = "openDoors(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;I)Z", at = @At("RETURN"), cancellable = true)
//    private void openDoorsHead(Level world, Block block, BlockPos checkPos, int dwellTicks, CallbackInfoReturnable<Boolean> cir) {
//        float doorOpenValue = ((MixinTrainAccessor) this).getDoorValue();
//        if (block instanceof IBlockPlatformDoor) {
//            if (doorOpenValue > 0.0f) {
//                ((IBlockPlatformDoor) block).setOpenState(true, doorOpenValue, world, checkPos, world.getBlockState(checkPos));
//                world.scheduleTick(checkPos, block, dwellTicks);
//            } else {
//                ((IBlockPlatformDoor) block).setOpenState(false, doorOpenValue, world, checkPos, world.getBlockState(checkPos));
//            }
//        }
//        if (block instanceof BlockPSDAPGDoorBase) {
//            for(int i = -1; i <= 1; ++i) {
//                BlockPos doorPos = checkPos.above(i);
//                BlockState state = world.getBlockState(doorPos);
//                Block doorBlock = state.getBlock();
//                BlockEntity entity = world.getBlockEntity(doorPos);
//                if (doorBlock instanceof BlockPSDAPGDoorBase && entity instanceof BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase && (Boolean) IBlock.getStatePropertySafe(state, BlockPSDAPGDoorBase.UNLOCKED)) {
//                    int doorStateValue = (int) Mth.clamp(doorOpenValue * 64.0F, 0.0F, 32.0F);
//                    ((BlockPSDAPGDoorBase.TileEntityPSDAPGDoorBase)entity).setOpen(doorStateValue);
//                    if (doorStateValue > 0 && !world.getBlockTicks().hasScheduledTick(doorPos, doorBlock)) {
//                        Utilities.scheduleBlockTick(world, doorPos, doorBlock, dwellTicks);
//                    }
//                }
//            }
//        }
//
////        return false;
//        cir.setReturnValue(false);
//    }
}
