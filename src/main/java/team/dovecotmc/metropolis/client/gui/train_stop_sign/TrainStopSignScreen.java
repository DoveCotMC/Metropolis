package team.dovecotmc.metropolis.client.gui.train_stop_sign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import team.dovecotmc.metropolis.client.network.MetroClientNetworking;

public class TrainStopSignScreen extends Screen {
    public static final ResourceLocation ATLAS = new ResourceLocation("metropolis", "textures/block/sign/train_stop_sign/gui.png");
    public static final int ATLAS_WIDTH = 64;
    public static final int ATLAS_HEIGHT = 64;
    public static final int BG_WIDTH = 128;
    public static final int BG_HEIGHT = 128;

    private int index;
    private final BlockPos blockPos;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = true;
    private boolean lastPressing = true;
    protected boolean pressed = false;

    public TrainStopSignScreen(int index, BlockPos blockPos) {
        super(Component.translatable("screen.metropolis.train_stop_sign"));
        this.index = index;
        this.blockPos = blockPos;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);

        guiGraphics.blit(
                ATLAS,
                guiGraphics.guiWidth() / 2 - BG_WIDTH / 2, guiGraphics.guiHeight() / 2 - BG_HEIGHT / 2,
                BG_WIDTH, BG_HEIGHT,
                0, 0,
                32, 32,
                ATLAS_WIDTH, ATLAS_HEIGHT
        );

        // Content
        if (index == 0) {
            guiGraphics.blit(
                    ATLAS,
                    guiGraphics.guiWidth() / 2 - BG_WIDTH / 2, guiGraphics.guiHeight() / 2 - BG_HEIGHT / 2,
                    BG_WIDTH, BG_HEIGHT,
                    0, 32,
                    32, 32,
                    ATLAS_WIDTH, ATLAS_HEIGHT
            );
        } else {
            int w = 72;
            int h = 72;
            guiGraphics.blit(
                    new ResourceLocation("metropolis", "textures/block/sign/train_stop_sign/numbers/" + index + ".png"),
                    guiGraphics.guiWidth() / 2 - w / 2, guiGraphics.guiHeight() / 2 - h / 2,
                    w, h,
                    0, 0,
                    9, 9,
                    9, 9
            );
        }

        // Buttons
        int bW = 8 * 4;
        int bH = 6 * 4;
        int upX = guiGraphics.guiWidth() / 2 - bW / 2;
        int upY = guiGraphics.guiHeight() / 2 - bH / 2 - (72 + 12);
        guiGraphics.blit(
                ATLAS,
                upX, upY,
                bW,  bH,
                32, 0,
                8, 6,
                ATLAS_WIDTH, ATLAS_HEIGHT
        );
        if (pressed && mouseX >= upX && mouseY >= upY && mouseX <= upX + bW && mouseY <= upY + bH) {
            nextIndex();
            playDownSound(Minecraft.getInstance().getSoundManager());
        }

        int downX = guiGraphics.guiWidth() / 2 - bW / 2;
        int downY = guiGraphics.guiHeight() / 2 - bH / 2 + (72 + 12);
        guiGraphics.blit(
                ATLAS,
                downX, downY,
                bW,  bH,
                32 + 8, 0,
                8, 6,
                ATLAS_WIDTH, ATLAS_HEIGHT
        );
        if (pressed && mouseX >= downX && mouseY >= downY && mouseX <= downX + bW && mouseY <= downY + bH) {
            previousIndex();
            playDownSound(Minecraft.getInstance().getSoundManager());
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
        this.pressing = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.pressing = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void nextIndex() {
        index = (index + 1) % (32 + 1);
    }

    private void previousIndex() {
        index = index - 1;

        if (index < 0)
            index += (32 + 1);

        index = index % (32 + 1);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        super.onClose();
        MetroClientNetworking.c2sTrainStopSignChanges(index, blockPos);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
