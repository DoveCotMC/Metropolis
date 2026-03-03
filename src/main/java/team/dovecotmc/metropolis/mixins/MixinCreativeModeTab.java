package team.dovecotmc.metropolis.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.dovecotmc.metropolis.Metropolis;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class MixinCreativeModeTab {
    @Shadow
    private static CreativeModeTab selectedTab;

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void met$localizationHint(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (!selectedTab.equals(Metropolis.ITEM_GROUP))
            return;

        Component component = Component.translatable("help.metropolis.localization");
        if (component.getString().isBlank()) {
            return;
        }

        int w = guiGraphics.guiWidth();
        int h = guiGraphics.guiHeight();

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                component,
                w / 2 - Minecraft.getInstance().font.width(component) / 2,
                (h / 2 - Minecraft.getInstance().font.lineHeight / 2 - 100) / 2,
                0xFFFFFF
        );
    }
}
