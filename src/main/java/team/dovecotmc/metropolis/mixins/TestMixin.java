package team.dovecotmc.metropolis.mixins;

import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.simulation.Simulator;
import org.mtr.mod.Init;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;

@Mixin(Simulator.class)
public class TestMixin {
    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            remap = false
    )
    private static void test(String dimension, String[] dimensions, Path rootPath, boolean threadedFileLoading, CallbackInfo ci) {
        try {
            throw new RuntimeException("1145141919810TEST!!!");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
