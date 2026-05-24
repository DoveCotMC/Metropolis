package team.dovecotmc.metropolis.client.gui.base;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import team.dovecotmc.metropolis.client.gui.base.components.ComponentBase;
import team.dovecotmc.metropolis.client.gui.base.components.IContainer;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMetropolisScreen extends Screen implements IContainer {
    private final List<ComponentBase> components;
    private int openTicks;
    private int mouseX = -1;
    private int mouseY = -1;

    private boolean lPressing = false;
    private boolean lastLPressing = false;
    private boolean lClicked = false;

    private boolean rPressing = false;
    private boolean lastRPressing = false;
    private boolean rClicked = false;

    private float width = 0;
    private float height = 0;

    public BaseMetropolisScreen(String id) {
        super(Component.translatable("gui.metropolis." + id + ".name"));
        this.components = new ArrayList<>();
        this.openTicks = 0;
    }

    @Override
    public void addComponent(ComponentBase component) {
        this.components.add(component);
    }

    @Override
    protected void init() {
        super.init();
        lPressing = InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_MOUSE_BUTTON_1);
        lastLPressing = lPressing;
    }

    @Override
    public void tick() {
        openTicks++;

        for (ComponentBase component : components) {
            component.tick();
        }

        super.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float tickDelta) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        lClicked = lPressing && !lastLPressing;
        lastLPressing = lPressing;
        rClicked = rPressing && !lastRPressing;
        lastRPressing = rPressing;

        this.width = guiGraphics.guiWidth();
        this.height = guiGraphics.guiHeight();

        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();

        // Scale to actual size
        float xFactor = (float) guiGraphics.guiWidth() / Minecraft.getInstance().getWindow().getWidth();
        float yFactor = (float) guiGraphics.guiHeight() / Minecraft.getInstance().getWindow().getHeight();
        poseStack.scale(xFactor, yFactor, 1f);
        RenderSystem.applyModelViewMatrix();

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(1 / xFactor, 1 / yFactor, 1f);

        guiGraphics.pose().pushPose();
        float factor = (float) Math.abs(Math.sin(getOpenTime() / 20f / Math.PI * 5f));
        guiGraphics.pose().scale(factor, factor, 1f);
        guiGraphics.drawString(Minecraft.getInstance().font, "test114514", 16, 16, 0xFFFFFFFF);
        guiGraphics.pose().popPose();

        for (ComponentBase component : components) {
            component.render(this, guiGraphics, getOpenTime());
        }

        guiGraphics.pose().popPose();
        poseStack.popPose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int key) {
        if (key == GLFW.GLFW_MOUSE_BUTTON_1) {
            lPressing = true;
        }

        if (key == GLFW.GLFW_MOUSE_BUTTON_2) {
            rPressing = true;
        }

        for (ComponentBase component : components) {
            if (component.isMouseIn((float) mouseX, (float) mouseY)) {
                component.onMouseDown(key);
            }
        }

        return super.mouseClicked(mouseX, mouseY, key);
    }

    @Override
    public boolean mouseReleased(double d, double e, int i) {
        if (i == GLFW.GLFW_MOUSE_BUTTON_1) {
            lPressing = false;
        }
        if (i == GLFW.GLFW_MOUSE_BUTTON_2) {
            rPressing = false;
        }
        return super.mouseReleased(d, e, i);
    }

    public int getOpenTicks() {
        return openTicks;
    }

    public float getOpenTime() {
        return openTicks + Minecraft.getInstance().getFrameTime();
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public @Nullable IContainer getParent() {
        return null;
    }

    @Override
    public BaseMetropolisScreen getRoot() {
        return this;
    }
}
