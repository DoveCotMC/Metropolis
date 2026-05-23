package team.dovecotmc.old.metropolis.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import team.dovecotmc.old.metropolis.OldMetropolis;
import team.dovecotmc.old.metropolis.util.MtrCommonUtil;

import java.util.List;
import java.util.Set;

public class MetropolisMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return OldMetropolis.shouldEnableMtrContent();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
