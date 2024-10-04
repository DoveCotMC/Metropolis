package team.dovecotmc.metropolis.client.gui.fare_adj;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
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
public class FareAdjPaymentScreen extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_payment/ticket_vendor_payment_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final Identifier CONTINUE_BUTTON_BASE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_payment/continue_button.png");
    private static final Identifier CONTINUE_BUTTON_BASE_HOVER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_payment/continue_button_hover.png");
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
        super(Text.translatable("gui.metropolis.fare_adj_payment.title"));
        this.pos = pos;
        this.paymentData = paymentData;
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        if (MinecraftClient.getInstance().world != null) {
            tipId = MinecraftClient.getInstance().world.random.nextInt(3);
        }
        MetroClientNetwork.updateCurrencyItem();
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

        // Render text
        // Title
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.fare_adj_payment.title").asOrderedText(),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                0x16161B,
                matrices.peek().getPositionMatrix(),
                immediate,
                15728880
        );
        immediate.draw();

        // Subtitle
        this.textRenderer.draw(
                matrices,
                Text.translatable("gui.metropolis.fare_adj_payment.subtitle"),
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

        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        for (Text text : paymentData.descriptions) {
            this.textRenderer.drawWithShadow(
                    matrices,
                    text,
                    intoTexturePosX(x0) / scaleFactor,
                    intoTexturePosY(y0 + (textRenderer.fontHeight + 2) * i0) / scaleFactor,
                    0xFFFFFF
            );
            i0++;
        }
        matrices.pop();

        // Description price
        Text priceText = Text.translatable("gui.metropolis.fare_adj_payment.price", paymentData.value);
//        scaleFactor = 1f;
        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        this.textRenderer.drawWithShadow(
                matrices,
                priceText,
                intoTexturePosX(x0) / scaleFactor,
                intoTexturePosY(y1 + 2) / scaleFactor,
                0xFFFFFF
        );

        int balance = 0;
        if (client != null && client.player != null) {
            // TODO: Configurable item
            balance = client.player.getInventory().count(MetroClientNetwork.currencyItem);
        }

        boolean ableToPay = balance >= paymentData.value;

        Text balanceText = Text.translatable("gui.metropolis.fare_adj_payment.balance", balance);
        this.textRenderer.drawWithShadow(
                matrices,
                balanceText,
                intoTexturePosX(x0) / scaleFactor,
                intoTexturePosY(y1 + 18) / scaleFactor,
                ableToPay ? 0xFFFFFF : 0xFF3F3F
        );

        // Item unit
        this.textRenderer.drawWithShadow(
                matrices,
                Text.literal("×"),
                intoTexturePosX(x0 + Math.max(textRenderer.getWidth(balanceText), textRenderer.getWidth(priceText)) + 4) / scaleFactor,
                intoTexturePosY(y1 + 2) / scaleFactor,
                0xFFFFFF
        );
        this.textRenderer.drawWithShadow(
                matrices,
                Text.literal("×"),
                intoTexturePosX(x0 + Math.max(textRenderer.getWidth(balanceText), textRenderer.getWidth(priceText)) + 4) / scaleFactor,
                intoTexturePosY(y1 + 18) / scaleFactor,
                ableToPay ? 0xFFFFFF : 0xFF3F3F
        );
        matrices.pop();

        this.itemRenderer.renderInGui(
                new ItemStack(MetroClientNetwork.currencyItem),
                intoTexturePosX(x0 + Math.max(textRenderer.getWidth(balanceText), textRenderer.getWidth(priceText)) + 4 + textRenderer.getWidth(Text.literal("×"))),
                intoTexturePosY(y1 - 4 + 1)
        );
        this.itemRenderer.renderInGui(
                new ItemStack(MetroClientNetwork.currencyItem),
                intoTexturePosX(x0 + Math.max(textRenderer.getWidth(balanceText), textRenderer.getWidth(priceText)) + 4 + textRenderer.getWidth(Text.literal("×"))),
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
        drawTexture(
                matrices,
                intoTexturePosX(x2),
                intoTexturePosY(y2 + CONTINUE_BUTTON_BASE_HEIGHT * 7),
                0,
                0,
                CONTINUE_BUTTON_BASE_WIDTH, CONTINUE_BUTTON_BASE_HEIGHT,
                CONTINUE_BUTTON_BASE_WIDTH, CONTINUE_BUTTON_BASE_HEIGHT
        );
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Text continueText = Text.translatable("gui.metropolis.fare_adj_payment.continue_button");
        textRenderer.draw(
                matrices,
                continueText,
                intoTexturePosX(x2 + CONTINUE_BUTTON_BASE_WIDTH / 2f - textRenderer.getWidth(continueText) / 2f) / scaleFactor,
                intoTexturePosY(y2 + CONTINUE_BUTTON_BASE_HEIGHT * 7 + 5) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();

        if (thisTabHovering && pressed && ableToPay) {
            playDownSound(MinecraftClient.getInstance().getSoundManager());
            client.setScreen(null);
            // TODO: Response
//            if (paymentData.type == FareAdjPaymentData.EnumTicketVendorPaymentType.SINGLE_TRIP) {
//                MetroClientNetwork.ticketVendorResult(pos, paymentData.resultStack, paymentData.value);
//            } else {
//                MetroClientNetwork.ticketVendorClose(pos, paymentData.resultStack, paymentData.value);
//            }
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
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        // TODO: Data transfer
        if (this.client != null) {
            this.client.setScreen(this.parentScreen);
        }
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
