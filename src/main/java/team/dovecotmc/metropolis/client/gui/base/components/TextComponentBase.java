package team.dovecotmc.metropolis.client.gui.base.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector4f;

public abstract class TextComponentBase extends ComponentBase {
    public Component text = Component.empty();
    public float fontSize = 7;
    public int textColor = 0xFFFFFFFF;
    public Vector4f padding = new Vector4f(3, 3, 4, 4);

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
        setPadding(y, y, x, x);
    }

    public void setPadding(float top, float bottom, float left, float right) {
        this.padding = new Vector4f(top, bottom, left, right);
    }

    public Vector4f getPadding() {
        return padding;
    }

    @Override
    public float getWidth() {
        float scaleFactor = getFontSize() / Minecraft.getInstance().font.lineHeight;
        return Math.max(super.getWidth() + getPadding().z + getPadding().w, Minecraft.getInstance().font.width(getText()) * scaleFactor + getPadding().z + getPadding().w);
    }

    @Override
    public float getHeight() {
        return Math.max(super.getHeight() + getPadding().x + getPadding().y, getFontSize() + getPadding().x + getPadding().y);
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
