package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

import java.util.Optional;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Environment(EnvType.CLIENT)
public class AlphaWarningHud extends DrawableHelper {
    public AlphaWarningHud() {
    }

    public void render(MatrixStack matrices, float tickDelta) {
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
        mc.textRenderer.drawWithShadow(matrices, MALocalizationUtil.literalText(mod.getMetadata().getName() + " " + mod.getMetadata().getVersion().getFriendlyString()), 4, 4, 0xFFFFFF);
        mc.textRenderer.drawWithShadow(matrices, MALocalizationUtil.literalText("This is an alpha version"), 4, 4 + (2 + mc.textRenderer.fontHeight), 0xFFFFFF);
        mc.textRenderer.drawWithShadow(matrices, MALocalizationUtil.literalText("It might cause incompatible issues"), 4, 4 + (2 + mc.textRenderer.fontHeight) * 2, 0xFFFFFF);
//        mc.textRenderer.drawWithShadow(matrices, MALocalizationUtil.literalText("Features in this version can be changed at any time"), 4, 4 + (2 + mc.textRenderer.fontHeight) * 3, 0xFFFFFF);
        matrices.pop();
    }
}
