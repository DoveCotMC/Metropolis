package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.network.MetroClientNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorScreenWarning extends Screen {
    private static final ResourceLocation BG_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_warning/ticket_vendor_warning_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation WARNING_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_warning/warning.png");
    protected static final int WARNING_TEXTURE_WIDTH = 32;
    protected static final int WARNING_TEXTURE_HEIGHT = 32;

    protected final BlockPos pos;

    protected TicketVendorScreenWarning(BlockPos pos) {
        super(MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_warning.title"));
        this.pos = pos;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, BG_TEXTURE_ID);
        blit(
                matrices,
                this.width / 2 - BG_TEXTURE_WIDTH / 2,
                this.height / 2 - BG_TEXTURE_HEIGHT / 2,
                0,
                0,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT
        );

        float scaleFactor = 1.5f;

        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        RenderSystem.setShaderTexture(0, WARNING_TEXTURE_ID);
        blit(
                matrices,
                (int) ((this.width / 2 - WARNING_TEXTURE_WIDTH / 2f * scaleFactor) / scaleFactor),
                (int) ((this.height / 2 - WARNING_TEXTURE_HEIGHT / 2f * scaleFactor - 16) / scaleFactor),
                0,
                0,
                WARNING_TEXTURE_WIDTH, WARNING_TEXTURE_HEIGHT,
                WARNING_TEXTURE_WIDTH, WARNING_TEXTURE_HEIGHT
        );
        matrices.popPose();

        scaleFactor = 14f / font.lineHeight;

        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Component warningTitle = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_warning.title_warning");
        font.drawShadow(
                matrices,
                warningTitle,
                (this.width / 2f - font.width(warningTitle) * scaleFactor / 2f) / scaleFactor,
                (this.height / 2f - font.lineHeight * scaleFactor / 2f + 28) / scaleFactor,
                0xFFFFFF
        );
        matrices.popPose();

        scaleFactor = 1f;

        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Component warningNoCard = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_warning.warning_no_card");
        font.drawShadow(
                matrices,
                warningNoCard,
                (this.width / 2f - font.width(warningNoCard) * scaleFactor / 2f) / scaleFactor,
                (this.height / 2f - font.lineHeight * scaleFactor / 2f + 44) / scaleFactor,
                0xFFFFFF
        );
        matrices.popPose();

        matrices.popPose();

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        super.onClose();

        if (minecraft != null && minecraft.level != null) {
            if (minecraft.level.getBlockEntity(pos) instanceof Container inventory) {
                if (!inventory.getItem(1).isEmpty()) {
                    MetroClientNetwork.ticketVendorClose(pos, inventory.getItem(1), 0);
                }
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
