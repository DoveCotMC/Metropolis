package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.Station;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketMachineScreen2 extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/ticket_vendor_2_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

//    private static final Identifier BUTTON_GREEN_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_green.png");
//    private static final Identifier BUTTON_GREEN_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_green_hover.png");
//    private static final Identifier BUTTON_PURPLE_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_purple.png");
//    private static final Identifier BUTTON_PURPLE_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_purple_hover.png");
//    private static final Identifier BUTTON_GRAY_1_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_1.png");
//    private static final Identifier BUTTON_GRAY_1_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_1_hover.png");
//    private static final Identifier BUTTON_GRAY_2_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_2.png");
//    private static final Identifier BUTTON_GRAY_2_HOVER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_1/button_gray_2_hover.png");
//    protected static final int BUTTON_BIG_WIDTH = 96;
//    protected static final int BUTTON_BIG_HEIGHT = 56;

    private static final Identifier STATION_TAB_BASE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/station_tab_base.png");
    private static final Identifier STATION_TAB_BASE_HOVER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/station_tab_base_hover.png");
    protected static final int BUTTON_SMALL_WIDTH = 150;
    protected static final int BUTTON_SMALL_HEIGHT = 16;

    protected final BlockPos pos;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = true;
    private boolean lastPressing = true;
    protected boolean pressed = false;

    protected String stationFrom = "";

    protected int tipId = 0;

    public TicketMachineScreen2(BlockPos pos, ItemStack ticket) {
        super(Text.translatable("gui.metropolis.ticket_vendor_2.title"));
        this.pos = pos;
//        this.ticketItem = ticketItem;
//        InventoryScreen
    }

    @Override
    protected void init() {
        if (MinecraftClient.getInstance().world != null) {
            tipId = MinecraftClient.getInstance().world.random.nextInt(3);
        }
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

        RenderSystem.disableBlend();

        // Render text
        // Title
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
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
                Text.translatable("gui.metropolis.ticket_vendor_2.subtitle"),
                intoTexturePosX(48),
                intoTexturePosY(35),
                0x3F3F3F
        );

        if (this.client != null && this.client.world != null) {
            for (Station station : MtrStationUtil.getAllStations(this.client.world)) {
//                System.out.println(station.name);
            }
        }

        super.render(matrices, mouseX, mouseY, delta);

        if (pressing) {
            if (!lastPressing) {
                pressed = true;
            } else {
                pressed = false;
            }
        } else {
            pressed = false;
        }
        lastPressing = pressing;

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
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.pressing = true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.pressing = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        // TODO: Data transfer
        this.client.setScreen(new TicketMachineScreen1(pos, ItemStack.EMPTY));
//        super.close();
//        ClientPlayNetworking.send(Metropolis.ID_SCREEN_CLOSE_TICKET_MACHINE, PacketByteBufs.create().writeItemStack(this.ticketItem));
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }
}
