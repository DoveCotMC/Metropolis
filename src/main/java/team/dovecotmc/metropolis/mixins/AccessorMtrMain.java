package team.dovecotmc.metropolis.mixins;

import org.mtr.core.Main;
import org.mtr.core.simulation.Simulator;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Main.class)
public interface AccessorMtrMain {
//    @Accessor
//    static ObjectImmutableList<Simulator> getSimulators() {
//        throw new AssertionError();
//    }
}
