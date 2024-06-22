package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
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

    protected int tipId = 0;

    public TicketMachineScreen(BlockPos pos, ItemStack ticket) {
        super(Text.translatable("metropolis.title.screen.ticket_vendor"));
//        this.ticketItem = ticketItem;
//        InventoryScreen
    }

    @Override
    protected void init() {
        if (MinecraftClient.getInstance().world != null) {
            tipId = MinecraftClient.getInstance().world.random.nextInt(3);
        }
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
        boolean greenHovering = this.mouseX >= x0 && this.mouseY >= y0 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (greenHovering) {
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
        boolean purpleHovering = this.mouseX >= x0 && this.mouseY >= y1 && this.mouseX <= x0 + BUTTON_BIG_WIDTH && this.mouseY <= y1 + BUTTON_BIG_HEIGHT;
        if (purpleHovering) {
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
        boolean grayTopHovering = this.mouseX >= x1 && this.mouseY >= y0 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y0 + BUTTON_BIG_HEIGHT;
        if (grayTopHovering) {
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
        boolean grayBottomHovering = this.mouseX >= x1 && this.mouseY >= y1 && this.mouseX <= x1 + BUTTON_BIG_WIDTH && this.mouseY <= y1 + BUTTON_BIG_HEIGHT;
        if (grayBottomHovering) {
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
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
//        BlockRenderContext
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor.title").asOrderedText(),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                0x3F3F3F,
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
                0x3F3F3F
        );

        // Refund tips
        matrices.push();
        float scaleFactor = 10f / 14f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
//        Text tip = Text.translatable("gui.metropolis.ticket_vendor.tips_" + tipId);
        float tipId = 0;
        if (this.client != null && this.client.world != null) {
            tipId = client.world.getTime() / 128f;
        }
        Text tip = Text.translatable("gui.metropolis.ticket_vendor.tips_" + ((int) tipId % 3));
        this.textRenderer.draw(
                matrices,
                tip,
                intoTexturePosX(50) / scaleFactor,
                intoTexturePosY(175) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        // Buttons
        // Left bar Buttons
        matrices.push();
//        scaleFactor = 10f / 14f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);

        // Buy Tickets
        this.textRenderer.draw(
                matrices,
                Text.translatable("gui.metropolis.ticket_vendor.button.buy_tickets"),
                intoTexturePosX(11) / scaleFactor,
                intoTexturePosY(31) / scaleFactor,
                0x3F3F3F
        );

        // Special
        this.textRenderer.draw(
                matrices,
                Text.translatable("gui.metropolis.ticket_vendor.button.special"),
                intoTexturePosX(11) / scaleFactor,
                intoTexturePosY(44) / scaleFactor,
                0x3F3F3F
        );

        // Refund
        this.textRenderer.draw(
                matrices,
                Text.translatable("gui.metropolis.ticket_vendor.button.refund"),
                intoTexturePosX(11) / scaleFactor,
                intoTexturePosY(180) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();

        // Centered
        matrices.push();
//        scaleFactor = 14f / 14f;
        scaleFactor = 1f;
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        // Tickets/Green
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor.button.tickets").asOrderedText(),
                intoTexturePosX(54) / scaleFactor + (greenHovering ? 1 : 0),
                intoTexturePosY(58) / scaleFactor + (greenHovering ? 1 : 0),
                0xFFFFFF,
                0x5EA919,
                matrices.peek().getPositionMatrix(),
                immediate,
                15728880
        );
        immediate.draw();

        // Charge/Purple
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor.button.charge").asOrderedText(),
                intoTexturePosX(54) / scaleFactor + (purpleHovering ? 1 : 0),
                intoTexturePosY(118) / scaleFactor + (purpleHovering ? 1 : 0),
                0xFFFFFF,
                0xA9309F,
                matrices.peek().getPositionMatrix(),
                immediate,
                15728880
        );
        immediate.draw();

        // Buy Commuter/Gray top
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor.button.buy_commuter").asOrderedText(),
                intoTexturePosX(152) / scaleFactor + (grayTopHovering ? 1 : 0),
                intoTexturePosY(58) / scaleFactor + (grayTopHovering ? 1 : 0),
                0xFFFFFF,
                0x3F4548,
                matrices.peek().getPositionMatrix(),
                immediate,
                15728880
        );
        immediate.draw();

        // Charge/Gray bottom
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor.button.buy_ic_card").asOrderedText(),
                intoTexturePosX(152) / scaleFactor + (grayBottomHovering ? 1 : 0),
                intoTexturePosY(118) / scaleFactor + (grayBottomHovering ? 1 : 0),
                0xFFFFFF,
                0x3F4548,
                matrices.peek().getPositionMatrix(),
                immediate,
                15728880
        );
        immediate.draw();
        matrices.pop();

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
