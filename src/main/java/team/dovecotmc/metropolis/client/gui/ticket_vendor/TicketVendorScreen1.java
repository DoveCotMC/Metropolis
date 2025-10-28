package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.network.MetroClientNetwork;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorScreen1 extends Screen {
    private static final ResourceLocation BG_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/ticket_vendor_1_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation BUTTON_GREEN_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_green.png");
    private static final ResourceLocation BUTTON_GREEN_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_green_hover.png");
    private static final ResourceLocation BUTTON_PURPLE_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_purple.png");
    private static final ResourceLocation BUTTON_PURPLE_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_purple_hover.png");
    private static final ResourceLocation BUTTON_GRAY_1_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_1.png");
    private static final ResourceLocation BUTTON_GRAY_1_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_1_hover.png");
    private static final ResourceLocation BUTTON_GRAY_2_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_2.png");
    private static final ResourceLocation BUTTON_GRAY_2_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_2_hover.png");
    protected static final int BUTTON_BIG_WIDTH = 96;
    protected static final int BUTTON_BIG_HEIGHT = 56;

    private static final ResourceLocation BUTTON_1_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_1.png");
    private static final ResourceLocation BUTTON_1_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_1_hover.png");
    private static final ResourceLocation BUTTON_2_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_2.png");
    private static final ResourceLocation BUTTON_2_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_2_hover.png");
    protected static final int BUTTON_SMALL_WIDTH = 33;
    protected static final int BUTTON_SMALL_HEIGHT = 12;

    protected final BlockPos pos;
    protected final TicketVendorData data;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = false;
    private boolean lastPressing = false;
    protected boolean pressed = false;

    protected int tipId = 0;

    public TicketVendorScreen1(BlockPos pos, TicketVendorData data) {
        super(MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.title"));
        this.pos = pos;
        this.data = data;
    }

    @Override
    protected void init() {
        if (Minecraft.getInstance().level != null) {
            tipId = Minecraft.getInstance().level.random.nextInt(3);
        }

        this.pressed = false;
        this.pressing = false;
        this.lastPressing = false;
        this.mouseX = 0;
        this.mouseY = 0;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.render(guiGraphics, mouseX, mouseY, delta);
        PoseStack matrices = guiGraphics.pose();

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

        int x0 = intoTexturePosX(48);
        int x1 = intoTexturePosX(146);
        int y0 = intoTexturePosY(47);
        int y1 = intoTexturePosY(107);
        // Four big centered buttons
        // Green
        boolean greenHovering = this.mouseX >= x0 && this.mouseY >= y0 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (greenHovering) {
            guiGraphics.blit(
                    BUTTON_GREEN_HOVER_TEXTURE_ID,
                    x0,
                    y0,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_GREEN_TEXTURE_ID,
                    x0,
                    y0,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        }

        // Purple
        boolean purpleHovering = this.mouseX >= x0 && this.mouseY >= y1 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y1 + BUTTON_BIG_HEIGHT;
        if (purpleHovering) {
            guiGraphics.blit(
                    BUTTON_PURPLE_HOVER_TEXTURE_ID,
                    x0,
                    y1,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_PURPLE_TEXTURE_ID,
                    x0,
                    y1,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        }

        // Gray Top
        boolean grayTopHovering = this.mouseX >= x1 && this.mouseY >= y0 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (grayTopHovering) {
            guiGraphics.blit(
                    BUTTON_GRAY_1_HOVER_TEXTURE_ID,
                    x1,
                    y0,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_GRAY_1_TEXTURE_ID,
                    x1,
                    y0,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        }

        // Gray Bottom
        boolean grayBottomHovering = this.mouseX >= x1 && this.mouseY >= y1 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y1 + BUTTON_BIG_HEIGHT;
        if (grayBottomHovering) {
            guiGraphics.blit(
                    BUTTON_GRAY_2_HOVER_TEXTURE_ID,
                    x1,
                    y1,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_GRAY_2_TEXTURE_ID,
                    x1,
                    y1,
                    0,
                    0,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                    BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
            );
        }

        // Left bar
        int x2 = intoTexturePosX(8);
        int y2a = intoTexturePosY(27);
        int y2b = intoTexturePosY(40);
        int y2z = intoTexturePosY(176);
        // Tickets
        if (this.mouseX >= x2 && this.mouseY >= y2a && this.mouseX <= x2 + BUTTON_SMALL_WIDTH && this.mouseY <= y2a + BUTTON_SMALL_HEIGHT) {
            guiGraphics.blit(
                    BUTTON_1_HOVER_TEXTURE_ID,
                    x2,
                    y2a,
                    0,
                    0,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_1_TEXTURE_ID,
                    x2,
                    y2a,
                    0,
                    0,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
            );
        }

        // Special
        if (this.mouseX >= x2 && this.mouseY >= y2b && this.mouseX <= x2 + BUTTON_SMALL_WIDTH && this.mouseY <= y2b + BUTTON_SMALL_HEIGHT) {
            guiGraphics.blit(
                    BUTTON_1_HOVER_TEXTURE_ID,
                    x2,
                    y2b,
                    0,
                    0,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_1_TEXTURE_ID,
                    x2,
                    y2b,
                    0,
                    0,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
            );
        }

        // Refund at bottom
        if (this.mouseX >= x2 && this.mouseY >= y2z && this.mouseX <= x2 + BUTTON_SMALL_WIDTH && this.mouseY <= y2z + BUTTON_SMALL_HEIGHT) {
            guiGraphics.blit(
                    BUTTON_2_HOVER_TEXTURE_ID,
                    x2,
                    y2z,
                    0,
                    0,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
            );
        } else {
            guiGraphics.blit(
                    BUTTON_2_TEXTURE_ID,
                    x2,
                    y2z,
                    0,
                    0,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                    BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
            );
        }

        RenderSystem.disableBlend();

        // Render text
        // Title
        MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor.title"),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                false
        );
        // TODO: Add outline effect if needed - the original code used drawInBatch8xOutline
        immediate.endBatch();

        // Subtitle
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.subtitle"),
                intoTexturePosX(48),
                intoTexturePosY(35),
                0x3F3F3F,
                false
        );

        // Refund tips
        matrices.pushPose();
        float scaleFactor = 10f / 14f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        float tipId = 0;
        if (this.minecraft != null && this.minecraft.level != null) {
            tipId = minecraft.level.getGameTime() / 128f;
        }
//        Text tip = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.tips_" + ((int) tipId % 3));
        String[] tips = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.tips").getString().split("\n");
        String tip = tips[((int) tipId % tips.length)];
        guiGraphics.drawString(
                this.font,
                tip,
                (int)(intoTexturePosX(50) / scaleFactor),
                (int)(intoTexturePosY(175) / scaleFactor),
                0xFFFFFF,
                false
        );
        matrices.popPose();

        // Buttons
        // Left bar Buttons
        matrices.pushPose();
//        scaleFactor = 10f / 14f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);

        // Buy Tickets
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.buy_tickets"),
                (int)(intoTexturePosX(11) / scaleFactor),
                (int)(intoTexturePosY(31) / scaleFactor),
                0x3F3F3F,
                false
        );

        // Special
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.special"),
                (int)(intoTexturePosX(11) / scaleFactor),
                (int)(intoTexturePosY(44) / scaleFactor),
                0x3F3F3F,
                false
        );

        // Refund
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.refund"),
                (int)(intoTexturePosX(11) / scaleFactor),
                (int)(intoTexturePosY(180) / scaleFactor),
                0xFFFFFF,
                false
        );
        matrices.popPose();

        // Centered
        matrices.pushPose();
//        scaleFactor = 14f / 14f;
        scaleFactor = 1f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        // Tickets/Green
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.tickets"),
                (int)(intoTexturePosX(54) / scaleFactor + (greenHovering ? 1 : 0)),
                (int)(intoTexturePosY(58) / scaleFactor + (greenHovering ? 1 : 0)),
                0xFFFFFF,
                false
        );
        // TODO: Add outline effect if needed

        // Charge/Purple
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.charge"),
                (int)(intoTexturePosX(54) / scaleFactor + (purpleHovering ? 1 : 0)),
                (int)(intoTexturePosY(118) / scaleFactor + (purpleHovering ? 1 : 0)),
                0xFFFFFF,
                false
        );

        // Buy Commuter/Gray top
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.buy_commuter"),
                (int)(intoTexturePosX(152) / scaleFactor + (grayTopHovering ? 1 : 0)),
                (int)(intoTexturePosY(58) / scaleFactor + (grayTopHovering ? 1 : 0)),
                0xFFFFFF,
                false
        );

        // Charge/Gray bottom
        guiGraphics.drawString(
                this.font,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_1.button.buy_ic_card"),
                (int)(intoTexturePosX(152) / scaleFactor + (grayBottomHovering ? 1 : 0)),
                (int)(intoTexturePosY(118) / scaleFactor + (grayBottomHovering ? 1 : 0)),
                0xFFFFFF,
                false
        );
        matrices.popPose();

        if (greenHovering && pressed) {
            if (this.minecraft.level != null) {
                playDownSound(Minecraft.getInstance().getSoundManager());
            }
            this.minecraft.setScreen(new TicketVendorScreen2(pos, this, this.data));
        }

        if (purpleHovering && pressed) {
            if (this.minecraft.level != null) {
                playDownSound(Minecraft.getInstance().getSoundManager());
            }

//            if (client.world.getBlockEntity(pos, MetroBlockEntities.TICKET_VENDOR_BLOCK_ENTITY).get().getStack(1).isEmpty()) {
            if (this.data.cardStack.isEmpty()) {
                this.minecraft.setScreen(new TicketVendorScreenWarning(pos));
            } else {
                this.minecraft.setScreen(new TicketVendorScreen4(pos, this, this.data));
            }
        }

        if (grayBottomHovering && pressed) {
            if (this.minecraft.level != null) {
                playDownSound(Minecraft.getInstance().getSoundManager());
            }

            this.minecraft.setScreen(new TicketVendorScreenBuyIC(pos, this, this.data));
        }

        // TODO: Other screens
        if (grayTopHovering) {
            guiGraphics.renderTooltip(this.font, MALocalizationUtil.literalText("Coming s∞n..."), mouseX, mouseY);
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
        super.onClose();

        if (minecraft != null && minecraft.level != null) {
            if (!data.cardStack.isEmpty()) {
                MetroClientNetwork.ticketVendorClose(pos, data.cardStack, 0);
            }
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