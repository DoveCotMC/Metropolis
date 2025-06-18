package team.dovecotmc.metropolis.client.gui.fare_adj;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class FareAdjScreenMain extends Screen {
    private static final ResourceLocation BG_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation BUTTON_GRAY_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_gray.png");
    private static final ResourceLocation BUTTON_GREEN_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_green.png");
    private static final ResourceLocation BUTTON_GREEN_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_green_hover.png");
    private static final ResourceLocation BUTTON_PURPLE_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_purple.png");
    private static final ResourceLocation BUTTON_PURPLE_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_purple_hover.png");
    protected static final int BUTTON_BIG_WIDTH = 104;
    protected static final int BUTTON_BIG_HEIGHT = 60;

    private static final ResourceLocation BUTTON_BLUE_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_blue.png");
    private static final ResourceLocation BUTTON_BLUE_HOVER_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_blue_hover.png");
    protected static final int BUTTON_WIDE_WIDTH = 220;
    protected static final int BUTTON_WIDE_HEIGHT = 48;

    protected final BlockPos pos;
    protected final FareAdjData data;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = false;
    private boolean lastPressing = false;
    protected boolean pressed = false;

    public FareAdjScreenMain(BlockPos pos, FareAdjData data) {
        super(MALocalizationUtil.translatableText("gui.metropolis.fare_adj_main.title"));
        this.pos = pos;
        this.data = data;
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
        boolean inserted = !this.data.ticketStack.isEmpty();
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

        // Subtitle
        this.font.draw(
                matrices,
                MALocalizationUtil.translatableText("gui.metropolis.fare_adj_main.subtitle"),
                intoTexturePosX(18),
                intoTexturePosY(32),
                0x3F3F3F
        );

        int x0 = intoTexturePosX(18);
        int x1 = intoTexturePosX(134);
        int y0 = intoTexturePosY(46);
        // Four big centered buttons
        // Purple
        boolean purpleHovering = this.mouseX >= x0 && this.mouseY >= y0 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (inserted) {
            if (purpleHovering) {
                RenderSystem.setShaderTexture(0, BUTTON_PURPLE_HOVER_TEXTURE_ID);
            } else {
                RenderSystem.setShaderTexture(0, BUTTON_PURPLE_TEXTURE_ID);
            }
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_GRAY_TEXTURE_ID);
        }
        blit(
                matrices,
                x0,
                y0,
                0,
                0,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
        );

        // Text
        matrices.pushPose();
        if (inserted) {
            if (purpleHovering) {
                matrices.translate(1, 1, 0);
            }
        }
        int i0 = 0;
        List<String> texts = new java.util.ArrayList<>(Arrays.stream(MALocalizationUtil.translatableText("gui.metropolis.fare_adj_main.ic_charge_button_text").getString().split("\n")).toList());
        Collections.reverse(texts);
        for (String text : texts) {
            this.font.drawInBatch8xOutline(
                    MALocalizationUtil.literalText(text).getVisualOrderText(),
                    x0 + (BUTTON_BIG_WIDTH / 2f - font.width(text) / 2f),
                    y0 + (BUTTON_BIG_HEIGHT - 20 - font.lineHeight * i0 - 2 * i0),
                    0xFFFFFF,
                    inserted ? 0xA9309F : 0xA9A9A9,
                    matrices.last().pose(),
                    immediate,
                    15728880
            );
            immediate.endBatch();
            i0++;
        }
        matrices.popPose();

        // Green
        boolean greenHovering = this.mouseX >= x1 && this.mouseY >= y0 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (inserted) {
            if (greenHovering) {
                RenderSystem.setShaderTexture(0, BUTTON_GREEN_HOVER_TEXTURE_ID);
            } else {
                RenderSystem.setShaderTexture(0, BUTTON_GREEN_TEXTURE_ID);
            }
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_GRAY_TEXTURE_ID);
        }
        blit(
                matrices,
                x1,
                y0,
                0,
                0,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
        );

        // Text
        matrices.pushPose();
        if (inserted) {
            if (greenHovering) {
                matrices.translate(1, 1, 0);
            }
        }
        i0 = 0;
        texts = new java.util.ArrayList<>(Arrays.stream(MALocalizationUtil.translatableText("gui.metropolis.fare_adj_main.ticket_charge_button_text").getString().split("\n")).toList());
        Collections.reverse(texts);
        for (String text : texts) {
            this.font.drawInBatch8xOutline(
                    MALocalizationUtil.literalText(text).getVisualOrderText(),
                    x1 + (BUTTON_BIG_WIDTH / 2f - font.width(text) / 2f),
                    y0 + (BUTTON_BIG_HEIGHT - 20 - font.lineHeight * i0 - 2 * i0),
                    0xFFFFFF,
                    inserted ? 0x5EA919 : 0xA9A9A9,
                    matrices.last().pose(),
                    immediate,
                    15728880
            );
            immediate.endBatch();
            i0++;
        }
        matrices.popPose();

        int y1 = intoTexturePosY(118);

        // Blue
        boolean blueHovering = this.mouseX >= x0 && this.mouseY >= y1 && this.mouseX <= x0 + BUTTON_WIDE_WIDTH && this.mouseY <= y1 + BUTTON_WIDE_HEIGHT;
        if (blueHovering) {
            RenderSystem.setShaderTexture(0, BUTTON_BLUE_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_BLUE_TEXTURE_ID);
        }
        blit(
                matrices,
                x0,
                y1,
                0,
                0,
                BUTTON_WIDE_WIDTH, BUTTON_WIDE_HEIGHT,
                BUTTON_WIDE_WIDTH, BUTTON_WIDE_HEIGHT
        );

        // Text
        matrices.pushPose();
        if (blueHovering) {
            matrices.translate(1, 1, 0);
        }
        i0 = 0;
        texts = new java.util.ArrayList<>(Arrays.stream(MALocalizationUtil.translatableText("gui.metropolis.fare_adj_main.no_ticket").getString().split("\n")).toList());
        Collections.reverse(texts);
        for (String text : texts) {
            this.font.drawInBatch8xOutline(
                    MALocalizationUtil.literalText(text).getVisualOrderText(),
                    x0 + 12,
                    y1 + (BUTTON_WIDE_HEIGHT - 24 - font.lineHeight * i0 - 2 * i0),
                    0xFFFFFF,
                    0x4C75DD,
                    matrices.last().pose(),
                    immediate,
                    15728880
            );
            immediate.endBatch();
            i0++;
        }
        matrices.popPose();

        super.render(matrices, mouseX, mouseY, delta);

        // Handle inputs
        if (minecraft != null) {
            if (pressed) {
                if (inserted) {
                    if (purpleHovering) {
                        // TODO: Fare adj charge event
                        minecraft.setScreen(null);
                        playButtonDownSound();
                    }
                }
                if (blueHovering) {
                    minecraft.setScreen(new FareAdjScreenNoTicket(pos, this.data, this));
                    playButtonDownSound();
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
        super.onClose();
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
