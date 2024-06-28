package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.client.network.MetroClientNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorScreenWarning extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_warning/ticket_vendor_warning_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final Identifier WARNING_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_warning/warning.png");
    protected static final int WARNING_TEXTURE_WIDTH = 32;
    protected static final int WARNING_TEXTURE_HEIGHT = 32;

    protected final BlockPos pos;

    protected TicketVendorScreenWarning(BlockPos pos) {
        super(Text.translatable("gui.metropolis.ticket_vendor_warning.title"));
        this.pos = pos;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, BG_TEXTURE_ID);
        drawTexture(
                matrices,
                this.width / 2 - BG_TEXTURE_WIDTH / 2,
                this.height / 2 - BG_TEXTURE_HEIGHT / 2,
                0,
                0,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT
        );

        float scaleFactor = 1.5f;

        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        RenderSystem.setShaderTexture(0, WARNING_TEXTURE_ID);
        drawTexture(
                matrices,
                (int) ((this.width / 2 - WARNING_TEXTURE_WIDTH / 2f * scaleFactor) / scaleFactor),
                (int) ((this.height / 2 - WARNING_TEXTURE_HEIGHT / 2f * scaleFactor - 16) / scaleFactor),
                0,
                0,
                WARNING_TEXTURE_WIDTH, WARNING_TEXTURE_HEIGHT,
                WARNING_TEXTURE_WIDTH, WARNING_TEXTURE_HEIGHT
        );
        matrices.pop();

        scaleFactor = 14f / textRenderer.fontHeight;

        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Text warningTitle = Text.translatable("gui.metropolis.ticket_vendor_warning.title_warning");
        textRenderer.drawWithShadow(
                matrices,
                warningTitle,
                (this.width / 2f - textRenderer.getWidth(warningTitle) * scaleFactor / 2f) / scaleFactor,
                (this.height / 2f - textRenderer.fontHeight * scaleFactor / 2f + 28) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();

        scaleFactor = 1f;

        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Text warningNoCard = Text.translatable("gui.metropolis.ticket_vendor_warning.warning_no_card");
        textRenderer.drawWithShadow(
                matrices,
                warningNoCard,
                (this.width / 2f - textRenderer.getWidth(warningNoCard) * scaleFactor / 2f) / scaleFactor,
                (this.height / 2f - textRenderer.fontHeight * scaleFactor / 2f + 44) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();

        matrices.pop();

        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();

        if (client != null && client.world != null) {
            if (client.world.getBlockEntity(pos) instanceof Inventory inventory) {
                if (!inventory.getStack(1).isEmpty()) {
                    MetroClientNetwork.ticketVendorClose(pos, inventory.getStack(1), 0);
                }
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
