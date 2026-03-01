package team.dovecotmc.metropolis.mixins;


import mtr.data.Train;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Train.class)
public interface MixinTrainAccessor {
    @Accessor(remap = false)
    float getDoorValue();
}