package team.dovecotmc.metropolis.client.gui.base.components;

import net.minecraft.client.gui.GuiGraphics;
import team.dovecotmc.metropolis.client.gui.base.IGuiEventHandler;

public abstract class ComponentBase implements IGuiEventHandler {
    public final IContainer parent;
    public float x = 0;
    public float y = 0;
    public float width = 0;
    public float height = 0;
    public int backgroundColor = 0x00000000;

    public ComponentBase(IContainer parent) {
        this.parent = parent;
        this.parent.addComponent(this);
    }

    public float getX() {
        return x;
    }

    public void setPosition(float x, float y) {
        setX(x);
        setY(y);
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void tick() {
    }

    public void render(IContainer parent, GuiGraphics guiGraphics, float time) {
        guiGraphics.fill((int) getX(), (int) getY(), (int) (getX() + getWidth()), (int) (getY() + getHeight()), getBackgroundColor());
    }

    public boolean isMouseIn(float mouseX, float mouseY) {
        return mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight();
    }

    @Override
    public void onMouseDown(int key) {
    }

    @Override
    public void onMouseRelease(int key) {
    }

    @Override
    public void onMouseEnter() {
    }

    @Override
    public void onMouseLeave() {
    }

    @Override
    public void onMouseScroll(double amount) {
    }
}
