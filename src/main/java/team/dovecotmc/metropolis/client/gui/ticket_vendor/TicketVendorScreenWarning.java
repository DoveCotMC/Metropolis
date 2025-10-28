package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        guiGraphics.fillGradient(0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.blit(
                BG_TEXTURE_ID,
                this.width / 2 - BG_TEXTURE_WIDTH / 2,
                this.height / 2 - BG_TEXTURE_HEIGHT / 2,
                0,
                0,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT
        );

        float scaleFactor = 1.5f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleFactor, scaleFactor, scaleFactor);
        guiGraphics.blit(
                WARNING_TEXTURE_ID,
                (int) ((this.width / 2 - WARNING_TEXTURE_WIDTH / 2f * scaleFactor) / scaleFactor),
                (int) ((this.height / 2 - WARNING_TEXTURE_HEIGHT / 2f * scaleFactor - 16) / scaleFactor),
                0,
                0,
                WARNING_TEXTURE_WIDTH, WARNING_TEXTURE_HEIGHT,
                WARNING_TEXTURE_WIDTH, WARNING_TEXTURE_HEIGHT
        );
        guiGraphics.pose().popPose();

        scaleFactor = 14f / font.lineHeight;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleFactor, scaleFactor, scaleFactor);
        Component warningTitle = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_warning.title_warning");
        guiGraphics.drawString(
                font,
                warningTitle,
                (int) ((this.width / 2f - font.width(warningTitle) * scaleFactor / 2f) / scaleFactor),
                (int) ((this.height / 2f - font.lineHeight * scaleFactor / 2f + 28) / scaleFactor),
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();

        scaleFactor = 1f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleFactor, scaleFactor, scaleFactor);
        Component warningNoCard = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_warning.warning_no_card");
        guiGraphics.drawString(
                font,
                warningNoCard,
                (int) ((this.width / 2f - font.width(warningNoCard) * scaleFactor / 2f) / scaleFactor),
                (int) ((this.height / 2f - font.lineHeight * scaleFactor / 2f + 44) / scaleFactor),
                0xFFFFFF,
                true
        );
        guiGraphics.pose().popPose();

        super.render(guiGraphics, mouseX, mouseY, delta);
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