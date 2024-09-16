package team.dovecotmc.metropolis.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.dovecotmc.metropolis.Metropolis;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderTail(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        MatrixStack matrices = new MatrixStack();
        MinecraftClient mc = MinecraftClient.getInstance();
        RenderSystem.assertOnRenderThread();
        Optional<ModContainer> modContainer = FabricLoader.getInstance().getModContainer(Metropolis.MOD_ID);
        if (modContainer.isEmpty()) {
            return;
        }
        ModContainer mod = modContainer.get();

        matrices.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        mc.textRenderer.drawWithShadow(matrices, Text.literal(mod.getMetadata().getName() + " " + mod.getMetadata().getVersion().getFriendlyString()), 4, 4, 0xFFFFFF);
        mc.textRenderer.drawWithShadow(matrices, Text.literal("This is an alpha version"), 4, 4 + (2 + mc.textRenderer.fontHeight), 0xFFFFFF);
        mc.textRenderer.drawWithShadow(matrices, Text.literal("It might cause incompatible issues"), 4, 4 + (2 + mc.textRenderer.fontHeight) * 2, 0xFFFFFF);
//        mc.textRenderer.drawWithShadow(matrices, Text.literal("Features in this version can be changed at any time"), 4, 4 + (2 + mc.textRenderer.fontHeight) * 3, 0xFFFFFF);
        matrices.pop();
    }
}
