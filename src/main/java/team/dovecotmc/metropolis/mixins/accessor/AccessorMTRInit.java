package team.dovecotmc.metropolis.mixins.accessor;

import org.mtr.core.Main;
import org.mtr.mod.Init;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Init.class, remap = false)
public interface AccessorMTRInit {
    @Accessor
    static Main getMain() {
        throw new RuntimeException("Mixin Error!");
    }
}
