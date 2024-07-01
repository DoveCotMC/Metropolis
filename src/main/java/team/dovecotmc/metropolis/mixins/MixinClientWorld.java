package team.dovecotmc.metropolis.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(ClientWorld.class)
public class MixinClientWorld {
    @Inject(at = @At("TAIL"), method = "setBlockState")
    public void setBlockStateTail(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        System.out.println(pos);
        System.out.println(state);
    }
}
