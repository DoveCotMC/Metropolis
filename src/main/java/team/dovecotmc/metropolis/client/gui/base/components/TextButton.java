package team.dovecotmc.metropolis.client.gui.base.components;

import net.minecraft.client.gui.GuiGraphics;

public class TextButton extends TextComponentBase {
    public Runnable func;

    public TextButton(IContainer parent, Runnable func) {
        super(parent);
        this.func = func;
    }

    @Override
    public void render(IContainer parent, GuiGraphics guiGraphics, float time) {
        this.setBackgroundColor(0x00000000);

        guiGraphics.blit(
                texture,
                (int) getX(), (int) getY(),
                0, 0,
                4, 4,
                128, 128
        );

        for (int i = 4; i < getWidth() - 4; i += 4) {
            guiGraphics.blit(
                    texture,
                    (int) getX() + i, (int) getY(),
                    4, 0,
                    4, 4,
                    128, 128
            );
        }
//        guiGraphics.blit(
//                texture,
//                (int) (getX() + (getWidth() - 8) + (getWidth() - 8) % 4), (int) getY(),
//                4, 0,
//                (int) ((getWidth() - 8) % 4), 4,
//                128, 128
//        );
        guiGraphics.blit(
                texture,
                (int) (getX() + getWidth() - 4), (int) getY(),
                12, 0,
                4, 4,
                128, 128
        );

        // Center
        for (int x = 4; x < getWidth() - 4; x += 4) {
            for (int y = 4; y < getHeight() - 4; y += 4) {
                guiGraphics.blit(
                        texture,
                        (int) getX() + x, (int) getY() + y,
                        4, 4,
                        4, 4,
                        128, 128
                );
            }
        }

        // Left
        for (int i = 4; i < getHeight() - 4; i += 4) {
            guiGraphics.blit(
                    texture,
                    (int) getX(), (int) getY() + i,
                    0, 4,
                    4, 4,
                    128, 128
            );
        }
        // Right
        for (int i = 4; i < getHeight() - 4; i += 4) {
            guiGraphics.blit(
                    texture,
                    (int) (getX() + getWidth() - 4), (int) getY() + i,
                    12, 4,
                    4, 4,
                    128, 128
            );
        }
        // Bottom
        for (int i = 4; i < getWidth() - 4; i += 4) {
            guiGraphics.blit(
                    texture,
                    (int) (getX() + i), (int) (getY() + getHeight() - 4),
                    4, 12,
                    4, 4,
                    128, 128
            );
        }
        guiGraphics.blit(
                texture,
                (int) getX(), (int) (getY() + getHeight() - 4),
                0, 12,
                4, 4,
                128, 128
        );
        guiGraphics.blit(
                texture,
                (int) (getX() + getWidth() - 4), (int) (getY() + getHeight() - 4),
                12, 12,
                4, 4,
                128, 128
        );

        super.render(parent, guiGraphics, time);
    }

    @Override
    public void onMouseDown(int key) {
        super.onMouseDown(key);
    }

    @Override
    public void onMouseRelease(int key) {
        super.onMouseRelease(key);
        func.run();
    }
}
