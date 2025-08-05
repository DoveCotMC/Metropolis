package team.dovecotmc.metropolis.client.gui.fare_adj;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.network.MetroClientNetwork;
import team.dovecotmc.metropolis.item.MetroItems;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class FareAdjScreenNoTicket extends Screen {
    public static final int MAXIMUM_PRICE = 500;
    private static final ResourceLocation BG_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_no_ticket/base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation WARNING_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_no_ticket/warning.png");
    private static final ResourceLocation INFO_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_no_ticket/info.png");

    private static final ResourceLocation NEXT_BUTTON_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_no_ticket/next_button.png");
    private static final ResourceLocation NEXT_BUTTON_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_no_ticket/next_button_hover.png");
    public static final int NEXT_BUTTON_WIDTH = 88;
    public static final int NEXT_BUTTON_HEIGHT = 20;

    protected final BlockPos pos;
    protected final FareAdjData data;
    protected final Screen parent;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = false;
    private boolean lastPressing = false;
    protected boolean pressed = false;

    public FareAdjScreenNoTicket(BlockPos pos, FareAdjData data, Screen parent) {
        super(MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.title"));
        this.pos = pos;
        this.data = data;
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.pressed = false;
        this.pressing = false;
        this.lastPressing = false;
        this.mouseX = 0;
        this.mouseY = 0;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        matrices.pushPose();
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

        // Title
        MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        this.font.drawInBatch8xOutline(
                title.getVisualOrderText(),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                0x16161B,
                matrices.last().pose(),
                immediate,
                15728880
        );
        immediate.endBatch();
        matrices.popPose();

        // Subtitle
        matrices.pushPose();
        float scaleFactor = 1.5f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        this.font.draw(
                matrices,
                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.subtitle"),
                intoTexturePosX(22) / scaleFactor,
                intoTexturePosY(34) / scaleFactor,
                0x3F3F3F
        );
        matrices.popPose();

        matrices.pushPose();
        // If you have receipt
        String[] texts = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.if_you_have_receipt").getString().split("\n");
        int i0 = 0;
        for (String text : texts) {
            this.font.draw(
                    matrices,
                    text,
                    intoTexturePosX(22),
                    intoTexturePosY(52) + (font.lineHeight + 2) * i0,
                    0x3F3F3F
            );
            i0++;
        }

        // Insert receipt warning
        int warningSize = 14;
        Component text0 = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.insert_receipt");
        this.font.draw(
                matrices,
                text0,
                intoTexturePosX(22) + warningSize + 4,
//                intoTexturePosX(0) + BG_TEXTURE_WIDTH - 12 - textRenderer.getWidth(text0),
                intoTexturePosY(52) + (font.lineHeight + 2) * i0 + 6,
                0x3F3F3F
        );
        RenderSystem.setShaderTexture(0, INFO_TEXTURE_ID);
        blit(
                matrices,
                intoTexturePosX(22),
//                intoTexturePosX(0) + BG_TEXTURE_WIDTH - 12 - textRenderer.getWidth(text0) - warningSize - 4,
                intoTexturePosY(52) + (font.lineHeight + 2) * i0 + 6 - (warningSize - font.lineHeight) / 2 - 2,
                warningSize, warningSize,
                warningSize, warningSize,
                warningSize, warningSize
        );
        matrices.popPose();

        matrices.pushPose();
        // If you don't have receipt
        texts = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.if_you_dont_have_receipt").getString().split("\n");
        int i1 = 0;
        for (String text : texts) {
            this.font.draw(
                    matrices,
                    text,
                    intoTexturePosX(22),
                    intoTexturePosY(52) + 48 + (font.lineHeight + 2) * i1,
                    0x3F3F3F
            );
            i1++;
        }

        // Insert receipt warning
        Component text1 = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.pay_fare");
        this.font.draw(
                matrices,
                text1,
                intoTexturePosX(22) + warningSize + 4,
//                intoTexturePosX(0) + BG_TEXTURE_WIDTH - 12 - textRenderer.getWidth(text1),
                intoTexturePosY(52) + 48 + (font.lineHeight + 2) * i1 + 6,
                0x3F3F3F
        );
        RenderSystem.setShaderTexture(0, INFO_TEXTURE_ID);
        blit(
                matrices,
                intoTexturePosX(22),
//                intoTexturePosX(0) + BG_TEXTURE_WIDTH - 12 - textRenderer.getWidth(text1) - warningSize - 4,
                intoTexturePosY(52) + 48 + (font.lineHeight + 2) * i1 + 6 - (warningSize - font.lineHeight) / 2 - 2,
                warningSize, warningSize,
                warningSize, warningSize,
                warningSize, warningSize
        );
        matrices.popPose();

        // Pay button
        matrices.pushPose();
        int x0 = intoTexturePosX(144);
        int y0 = intoTexturePosY(152);
        boolean nextHovering = this.mouseX >= x0 && this.mouseY >= y0 && this.mouseX <= x0 + NEXT_BUTTON_WIDTH && this.mouseY <= y0 + NEXT_BUTTON_HEIGHT;
        if (nextHovering) {
            RenderSystem.setShaderTexture(0, NEXT_BUTTON_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, NEXT_BUTTON_TEXTURE_ID);
        }
        blit(
                matrices,
                x0,
                y0,
                0,
                0,
                NEXT_BUTTON_WIDTH, NEXT_BUTTON_HEIGHT,
                NEXT_BUTTON_WIDTH, NEXT_BUTTON_HEIGHT
        );

        Component text2 = MALocalizationUtil.translatableText("gui.metropolis.fare_adj_no_ticket.pay_button");
        this.font.draw(
                matrices,
                text2,
                x0 + NEXT_BUTTON_WIDTH / 2f - font.width(text2) / 2f,
                y0 + NEXT_BUTTON_HEIGHT / 2f - font.lineHeight / 2f,
                0x3F3F3F
        );
        matrices.popPose();

        super.render(matrices, mouseX, mouseY, delta);

        // Handle inputs
        if (minecraft != null) {
            if (pressed) {
                if (nextHovering) {
                    playButtonDownSound();
//                    client.setScreen(new FareAdjScreenPayFare(pos, data, this));
                    int price = MetroClientNetwork.maxFare;
                    minecraft.setScreen(new FareAdjPaymentScreen(
                            pos,
                            new FareAdjPaymentData(FareAdjPaymentData.EnumTicketVendorPaymentType.PAY_FARE, price, new Component[]{
                                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.single_trip.title"),
                                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.single_trip.ticket_value", price),
                                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_payment.single_trip.amount", 1)
                            }, new ItemStack(MetroItems.ITEM_EXIT_TICKET)),
                            this
                    ));
                }
            }
        }

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
        if (button == 0)
            this.pressing = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0)
            this.pressing = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }

    public void playButtonDownSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
