package team.dovecotmc.metropolis.client.gui.fare_adj;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.network.MetroClientNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class FareAdjPaymentScreen extends Screen {
    private static final ResourceLocation BG_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_payment/ticket_vendor_payment_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation CONTINUE_BUTTON_BASE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_payment/continue_button.png");
    private static final ResourceLocation CONTINUE_BUTTON_BASE_HOVER_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_payment/continue_button_hover.png");
    protected static final int CONTINUE_BUTTON_BASE_WIDTH = 56;
    protected static final int CONTINUE_BUTTON_BASE_HEIGHT = 16;

    protected final BlockPos pos;
    protected final FareAdjPaymentData paymentData;
    protected final Screen parentScreen;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = true;
    private boolean lastPressing = true;
    protected boolean pressed = false;

    protected int tipId = 0;

    public FareAdjPaymentScreen(BlockPos pos, FareAdjPaymentData paymentData, Screen parentScreen) {
        super(MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.title"));
        this.pos = pos;
        this.paymentData = paymentData;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        if (Minecraft.getInstance().level != null) {
            tipId = Minecraft.getInstance().level.random.nextInt(3);
        }
        MetroClientNetwork.updateMaxFare();
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

        // Render text
        // Title
        MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        this.font.drawInBatch8xOutline(
                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.title").getVisualOrderText(),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                0x16161B,
                matrices.last().pose(),
                immediate,
                15728880
        );
        immediate.endBatch();

        // Subtitle
        this.font.draw(
                matrices,
                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.subtitle"),
                intoTexturePosX(34),
                intoTexturePosY(35),
                0x3F3F3F
        );

        // Description ticket
//        float scaleFactor = 8f / textRenderer.fontHeight;
        float scaleFactor = 1f;
        int x0 = 36 + 4;
        int y0 = 51 + 4;
        int y1 = 138 + 4;
        int i0 = 0;

        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        for (Component text : paymentData.descriptions) {
            this.font.drawShadow(
                    matrices,
                    text,
                    intoTexturePosX(x0) / scaleFactor,
                    intoTexturePosY(y0 + (font.lineHeight + 2) * i0) / scaleFactor,
                    0xFFFFFF
            );
            i0++;
        }
        matrices.popPose();

        // Description price
        Component priceText = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.price", paymentData.value);
//        scaleFactor = 1f;
        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        this.font.drawShadow(
                matrices,
                priceText,
                intoTexturePosX(x0) / scaleFactor,
                intoTexturePosY(y1 + 2) / scaleFactor,
                0xFFFFFF
        );

        int balance = 0;
        if (minecraft != null && minecraft.player != null) {
            // TODO: Configurable item
            balance = minecraft.player.getInventory().countItem(MetroClientNetwork.currencyItem);
        }

        boolean ableToPay = balance >= paymentData.value;

        Component balanceText = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.balance", balance);
        this.font.drawShadow(
                matrices,
                balanceText,
                intoTexturePosX(x0) / scaleFactor,
                intoTexturePosY(y1 + 18) / scaleFactor,
                ableToPay ? 0xFFFFFF : 0xFF3F3F
        );

        // Item unit
        this.font.drawShadow(
                matrices,
                MALocalizationUtil.literalText("×"),
                intoTexturePosX(x0 + Math.max(font.width(balanceText), font.width(priceText)) + 4) / scaleFactor,
                intoTexturePosY(y1 + 2) / scaleFactor,
                0xFFFFFF
        );
        this.font.drawShadow(
                matrices,
                MALocalizationUtil.literalText("×"),
                intoTexturePosX(x0 + Math.max(font.width(balanceText), font.width(priceText)) + 4) / scaleFactor,
                intoTexturePosY(y1 + 18) / scaleFactor,
                ableToPay ? 0xFFFFFF : 0xFF3F3F
        );
        matrices.popPose();

        this.itemRenderer.renderAndDecorateFakeItem(
                new ItemStack(MetroClientNetwork.currencyItem),
                intoTexturePosX(x0 + Math.max(font.width(balanceText), font.width(priceText)) + 4 + font.width(MALocalizationUtil.literalText("×"))),
                intoTexturePosY(y1 - 4 + 1)
        );
        this.itemRenderer.renderAndDecorateFakeItem(
                new ItemStack(MetroClientNetwork.currencyItem),
                intoTexturePosX(x0 + Math.max(font.width(balanceText), font.width(priceText)) + 4 + font.width(MALocalizationUtil.literalText("×"))),
                intoTexturePosY(y1 - 4 + 16 + 1)
        );

        // Right part
        int x2 = 176;
        int y2 = 51;

        // Continue button
        boolean thisTabHovering = this.mouseX >= intoTexturePosX(x2) && this.mouseY >= intoTexturePosY(y2 + CONTINUE_BUTTON_BASE_HEIGHT * 7) && this.mouseX <= intoTexturePosX(x2 + CONTINUE_BUTTON_BASE_WIDTH) && this.mouseY <= intoTexturePosY(y2 + CONTINUE_BUTTON_BASE_HEIGHT * 7 + CONTINUE_BUTTON_BASE_HEIGHT);
        if (thisTabHovering) {
            RenderSystem.setShaderTexture(0, CONTINUE_BUTTON_BASE_HOVER_ID);
        } else {
            RenderSystem.setShaderColor(241f / 256f, 175f / 256f, 21f / 256f, 1f);
            RenderSystem.setShaderTexture(0, CONTINUE_BUTTON_BASE_ID);
        }
        blit(
                matrices,
                intoTexturePosX(x2),
                intoTexturePosY(y2 + CONTINUE_BUTTON_BASE_HEIGHT * 7),
                0,
                0,
                CONTINUE_BUTTON_BASE_WIDTH, CONTINUE_BUTTON_BASE_HEIGHT,
                CONTINUE_BUTTON_BASE_WIDTH, CONTINUE_BUTTON_BASE_HEIGHT
        );
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Component continueText = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.continue_button");
        font.draw(
                matrices,
                continueText,
                intoTexturePosX(x2 + CONTINUE_BUTTON_BASE_WIDTH / 2f - font.width(continueText) / 2f) / scaleFactor,
                intoTexturePosY(y2 + CONTINUE_BUTTON_BASE_HEIGHT * 7 + 5) / scaleFactor,
                0xFFFFFF
        );
        matrices.popPose();

        if (thisTabHovering && pressed && ableToPay) {
            playDownSound(Minecraft.getInstance().getSoundManager());
            minecraft.setScreen(null);
            // TODO: Response
            if (paymentData.type == FareAdjPaymentData.EnumTicketVendorPaymentType.PAY_FARE) {
                MetroClientNetwork.fareAdjClose(pos, paymentData.resultStack, paymentData.value);
            } else {
                MetroClientNetwork.fareAdjClose(pos, paymentData.resultStack, paymentData.value);
            }
        }

        RenderSystem.disableBlend();

        super.render(matrices, mouseX, mouseY, delta);

        if (pressing) {
            pressed = !lastPressing;
        } else {
            pressed = false;
        }
        lastPressing = pressing;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.pressing = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.pressing = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        // TODO: Data transfer
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parentScreen);
        }
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
