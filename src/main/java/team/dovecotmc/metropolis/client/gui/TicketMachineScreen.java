package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.item.MetroItems;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketMachineScreen extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/ticket_vendor_1_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final Identifier BUTTON_GREEN_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_green.png");
    private static final Identifier BUTTON_GREEN_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_green_hover.png");
    private static final Identifier BUTTON_PURPLE_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_purple.png");
    private static final Identifier BUTTON_PURPLE_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_purple_hover.png");
    private static final Identifier BUTTON_GRAY_1_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_1.png");
    private static final Identifier BUTTON_GRAY_1_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_1_hover.png");
    private static final Identifier BUTTON_GRAY_2_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_2.png");
    private static final Identifier BUTTON_GRAY_2_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_2_hover.png");
    protected static final int BUTTON_BIG_WIDTH = 96;
    protected static final int BUTTON_BIG_HEIGHT = 56;

    private static final Identifier BUTTON_1_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_1.png");
    private static final Identifier BUTTON_1_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_1_hover.png");
    private static final Identifier BUTTON_2_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_2.png");
    private static final Identifier BUTTON_2_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_2_hover.png");
    protected static final int BUTTON_SMALL_WIDTH = 33;
    protected static final int BUTTON_SMALL_HEIGHT = 12;

    protected double mouseX = 0;
    protected double mouseY = 0;

    public TicketMachineScreen(BlockPos pos, ItemStack ticket) {
        super(Text.translatable("metropolis.title.screen.ticket_vendor"));
//        this.ticketItem = ticketItem;
//        InventoryScreen
    }

    @Override
    protected void init() {
//        ButtonWidget buttonTest = new ButtonWidget(0, 0, 128, 20, Text.translatable("metropolis.screen.ticket_machine.button.test"), button -> {
////            NbtCompound nbt = this.ticketItem.getOrCreateNbt();
////            nbt.putInt(ItemTicket.REMAIN_MONEY, nbt.getInt(ItemTicket.REMAIN_MONEY) + 1);
//            System.out.println(114514);
//        });
//        addDrawableChild(buttonTest);
//        SliderWidget sliderWidget = new SliderWidget(0, 20, 128, 20, Text.literal("wow"), 0.5) {
//            @Override
//            protected void updateMessage() {
//            }
//
//            @Override
//            protected void applyValue() {
//
//            }
//        };
//        addDrawableChild(sliderWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

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

        int x0 = intoTexturePosX(48);
        int x1 = intoTexturePosX(146);
        int y0 = intoTexturePosY(47);
        int y1 = intoTexturePosY(107);
        // Four big centered buttons
        // Green
        if (this.mouseX >= x0 && this.mouseY >= y0 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_GREEN_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_GREEN_TEXTURE_ID);
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

        // Purple
        if (this.mouseX >= x0 && this.mouseY >= y1 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y1 + BUTTON_BIG_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_PURPLE_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_PURPLE_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x0,
                y1,
                0,
                0,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
        );

        // Gray Top
        if (this.mouseX >= x1 && this.mouseY >= y0 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_GRAY_1_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_GRAY_1_TEXTURE_ID);
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

        // Gray Bottom
        if (this.mouseX >= x1 && this.mouseY >= y1 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y1 + BUTTON_BIG_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_GRAY_2_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_GRAY_2_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x1,
                y1,
                0,
                0,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT,
                BUTTON_BIG_WIDTH, BUTTON_BIG_HEIGHT
        );

        // Left bar
        int x2 = intoTexturePosX(8);
        int y2a = intoTexturePosY(27);
        int y2b = intoTexturePosY(40);
        int y2z = intoTexturePosY(176);
        // Tickets
        if (this.mouseX >= x2 && this.mouseY >= y2a && this.mouseX <= x2 + BUTTON_SMALL_WIDTH && this.mouseY <= y2a + BUTTON_SMALL_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_1_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_1_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x2,
                y2a,
                0,
                0,
                BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
        );

        // Special
        if (this.mouseX >= x2 && this.mouseY >= y2b && this.mouseX <= x2 + BUTTON_SMALL_WIDTH && this.mouseY <= y2b + BUTTON_SMALL_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_1_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_1_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x2,
                y2b,
                0,
                0,
                BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
        );

        // Refund at bottom
        if (this.mouseX >= x2 && this.mouseY >= y2z && this.mouseX <= x2 + BUTTON_SMALL_WIDTH && this.mouseY <= y2z + BUTTON_SMALL_HEIGHT) {
            RenderSystem.setShaderTexture(0, BUTTON_2_HOVER_TEXTURE_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_2_TEXTURE_ID);
        }
        drawTexture(
                matrices,
                x2,
                y2z,
                0,
                0,
                BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT,
                BUTTON_SMALL_WIDTH, BUTTON_SMALL_HEIGHT
        );

        RenderSystem.disableBlend();

        // Render text
        // Title
//        this.textRenderer.draw(
//                matrices,
//                Text.translatable("gui.metropolis.ticket_vendor.subtitle"),
//                intoTexturePosX(36),
//                intoTexturePosY(13),
//                0xFFFFFF
//        );
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        // 15728880
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor.title").asOrderedText(),
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
                Text.translatable("gui.metropolis.ticket_vendor.subtitle"),
                intoTexturePosX(48),
                intoTexturePosY(35),
                0x16161B
        );

        super.render(matrices, mouseX, mouseY, delta);

//        MatrixStack matrixStack = RenderSystem.getModelViewStack();
//        RenderSystem.applyModelViewMatrix();
//        matrixStack.push();
//
//        float time = 0;
//        if (mc.world != null) {
//            time = mc.world.getTime();
//        }
//
//        matrixStack.translate(0f, MathHelper.sin((time + mc.getTickDelta()) / 4f) * 4f, 0f);
//        int scaleFactor = 4;
//        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
//
//        ItemStack stack = new ItemStack(MetroItems.ITEM_TICKET);
//        itemRenderer.renderInGui(stack, (width / 2 - 16 * scaleFactor / 2) / scaleFactor, (height / 2 - 16 * scaleFactor / 2) / scaleFactor);
//
//        itemRenderer.renderGuiItemOverlay(textRenderer, stack, (width / 2 - 16 * scaleFactor / 2) / scaleFactor, (height / 2 - 16 * scaleFactor / 2) / scaleFactor);
//
//        matrixStack.pop();
//        RenderSystem.applyModelViewMatrix();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        super.mouseMoved(mouseX, mouseY);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        super.close();
//        ClientPlayNetworking.send(Metropolis.ID_SCREEN_CLOSE_TICKET_MACHINE, PacketByteBufs.create().writeItemStack(this.ticketItem));
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }
}
