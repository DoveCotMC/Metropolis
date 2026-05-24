package team.dovecotmc.metropolis.client.gui.base.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import team.dovecotmc.metropolis.Metropolis;

public abstract class TextComponentBase extends ComponentBase {
    public ResourceLocation texture = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/base/atlas.png");
    public Component text = Component.empty();
    public float fontSize = 7;
    public int textColor = 0xFFFFFFFF;
    public float paddingX = 6;
    public float paddingY = 4;

    public TextComponentBase(IContainer parent) {
        super(parent);
    }

    public void setText(Component text, float fontSize, int textColor) {
        setText(text);
        setFontSize(fontSize);
        setTextColor(textColor);
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Component getText() {
        return text;
    }

    public void setText(Component text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setPadding(float x, float y) {
        setPaddingX(x);
        setPaddingY(y);
    }

    public float getPaddingX() {
        return paddingX;
    }

    public void setPaddingX(float paddingX) {
        this.paddingX = paddingX;
    }

    public float getPaddingY() {
        return paddingY;
    }

    public void setPaddingY(float paddingY) {
        this.paddingY = paddingY;
    }

    @Override
    public float getWidth() {
        float scaleFactor = getFontSize() / Minecraft.getInstance().font.lineHeight;
        return Math.max(super.getWidth() + getPaddingX() * 2, Minecraft.getInstance().font.width(getText()) * scaleFactor + getPaddingY() * 2);
    }

    @Override
    public float getHeight() {
        return Math.max(super.getHeight() + getPaddingX() * 2, getFontSize() + getPaddingY() * 2);
    }

    @Override
    public void render(IContainer parent, GuiGraphics guiGraphics, float time) {
        super.render(parent, guiGraphics, time);

        Font font = Minecraft.getInstance().font;
        float scaleFactor = getFontSize() / font.lineHeight;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scaleFactor, scaleFactor, 1f);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                getText(),
                (int) ((getX() + getWidth() / 2f - (Minecraft.getInstance().font.width(getText()) * scaleFactor) / 2f) / scaleFactor),
                (int) ((getY() + getHeight() / 2f - getFontSize() / 2f) / scaleFactor),
                getTextColor()
        );
        guiGraphics.pose().popPose();
    }
}
