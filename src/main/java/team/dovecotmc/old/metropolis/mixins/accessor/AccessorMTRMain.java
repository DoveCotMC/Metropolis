package team.dovecotmc.old.metropolis.mixins.accessor;

import org.mtr.core.Main;
import org.mtr.core.simulation.Simulator;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Main.class, remap = false)
public interface AccessorMTRMain {
    @Accessor(remap = false)
    ObjectImmutableList<Simulator> getSimulators();
}
