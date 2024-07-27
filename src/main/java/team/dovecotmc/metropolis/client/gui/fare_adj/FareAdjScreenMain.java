package team.dovecotmc.metropolis.client.gui.fare_adj;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.OrderedText;
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
public class FareAdjScreenMain extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final Identifier BUTTON_GREEN_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_green.png");
    private static final Identifier BUTTON_GREEN_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_green_hover.png");
    private static final Identifier BUTTON_PURPLE_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_purple.png");
    private static final Identifier BUTTON_PURPLE_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_purple_hover.png");
    protected static final int BUTTON_BIG_WIDTH = 104;
    protected static final int BUTTON_BIG_HEIGHT = 60;

    private static final Identifier BUTTON_BLUE_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_blue.png");
    private static final Identifier BUTTON_BLUE_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/fare_adj_main/button_blue_hover.png");
    protected static final int BUTTON_WIDE_WIDTH = 220;
    protected static final int BUTTON_WIDE_HEIGHT = 48;

    protected final BlockPos pos;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = false;
    private boolean lastPressing = false;
    protected boolean pressed = false;

    public FareAdjScreenMain(BlockPos pos) {
        super(Text.translatable("gui.metropolis.fare_adj_main.title"));
        this.pos = pos;
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

        // Title
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        this.textRenderer.drawWithOutline(
                title.asOrderedText(),
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
                Text.translatable("gui.metropolis.fare_adj_main.subtitle"),
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
        if (purpleHovering) {
            RenderSystem.setShaderTexture(0, BUTTON_PURPLE_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_PURPLE_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x0,
                y0,
                0,
                0,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
        );

        // Text
        int i0 = 0;
        for (String text : Text.translatable("gui.metropolis.fare_adj_main.charge_button_text").getString().split("\n")) {
            System.out.println(text);
            this.textRenderer.drawWithOutline(
                    Text.literal(text).asOrderedText(),
                    intoTexturePosX(18) + (BUTTON_BIG_WIDTH / 2f - textRenderer.getWidth(text) / 2f),
                    intoTexturePosY((46 + BUTTON_BIG_HEIGHT - 7) - textRenderer.fontHeight * (i0 + 1)),
                    0xFFFFFF,
                    0xA9309F,
                    matrices.peek().getPositionMatrix(),
                    immediate,
                    15728880
            );
            immediate.draw();
            i0++;
        }

        // Green
        boolean greenHovering = this.mouseX >= x1 && this.mouseY >= y0 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (greenHovering) {
            RenderSystem.setShaderTexture(0, BUTTON_GREEN_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_GREEN_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x1,
                y0,
                0,
                0,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
        );

        int y1 = intoTexturePosY(118);

        // Blue
        boolean blueHovering = this.mouseX >= x0 && this.mouseY >= y1 && this.mouseX <= x0 + BUTTON_WIDE_WIDTH && this.mouseY <= y1 + BUTTON_WIDE_HEIGHT;
        if (blueHovering) {
            RenderSystem.setShaderTexture(0, BUTTON_BLUE_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_BLUE_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x0,
                y1,
                0,
                0,
                BUTTON_WIDE_WIDTH, BUTTON_WIDE_HEIGHT,
                BUTTON_WIDE_WIDTH, BUTTON_WIDE_HEIGHT
        );

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
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        super.close();
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
