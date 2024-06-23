package team.dovecotmc.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.Station;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.util.MtrStationUtil;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketMachineScreen3 extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/ticket_vendor_3_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final Identifier STATION_TAB_BASE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/station_tab_base.png");
    private static final Identifier STATION_TAB_BASE_HOVER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/station_tab_base_hover.png");
    protected static final int STATION_TAB_BASE_WIDTH = 48;
    protected static final int STATION_TAB_BASE_HEIGHT = 16;

    protected static final int MAX_VISIBLE = 8;

    private static final Identifier SLIDER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/slider.png");
    protected static final int SLIDER_WIDTH = 6;
    protected static final int SLIDER_HEIGHT = 9;

    protected final BlockPos pos;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = true;
    private boolean lastPressing = true;
    protected boolean pressed = false;

    protected Set<Station> stations;
    protected int sliderPos = 0;
    protected String stationFrom = "";

    protected int tipId = 0;

    public TicketMachineScreen3(BlockPos pos, ItemStack ticket) {
        super(Text.translatable("gui.metropolis.ticket_vendor_2.title"));
        this.pos = pos;
        if (this.client != null && this.client.world != null) {
            this.stations = MtrStationUtil.getStations(this.client.world);
        } else {
            this.stations = new HashSet<>();
        }
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

        // Station selection
        float scaleFactor = 10f / 14f;
        if (this.client != null && this.client.world != null) {
            stations = MtrStationUtil.getStations(this.client.world);
//            final int maxStrWidth = Objects.equals(stationFrom, "") ? 112 : 96;
            final int maxStrWidth = 32;
            int h0 = 128;
            int x0 = 24;
            int y0 = 51;
            int i0 = -sliderPos;
            int j0 = 0;

            // Slider
            int h1 = 119;
            int x1 = 76;
            int y1 = (int) (51 + (float) sliderPos / (float) (stations.size() - MAX_VISIBLE) * h1);

            RenderSystem.setShaderTexture(0, SLIDER_ID);
            drawTexture(
                    matrices,
                    intoTexturePosX(x1),
                    intoTexturePosY(y1),
                    0,
                    0,
                    SLIDER_WIDTH, SLIDER_HEIGHT,
                    SLIDER_WIDTH, SLIDER_HEIGHT
            );

            for (Station station : stations) {
                // Station base
                if (i0 < 0) {
                    i0++;
                    continue;
                }

                if (i0 >= MAX_VISIBLE) {
                    break;
                }

                float r = ColorHelper.Argb.getRed(station.color);
                float g = ColorHelper.Argb.getGreen(station.color);
                float b = ColorHelper.Argb.getBlue(station.color);
                RenderSystem.setShaderColor(r / 256f, g / 256f, b / 255f, 1f);
                boolean thisTabHovering = this.mouseX >= intoTexturePosX(x0) && this.mouseY >= intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0) && this.mouseX <= intoTexturePosX(x0 + STATION_TAB_BASE_WIDTH) && this.mouseY <= intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + STATION_TAB_BASE_HEIGHT);
                if (thisTabHovering) {
                    RenderSystem.setShaderTexture(0, STATION_TAB_BASE_HOVER_ID);
                } else {
                    RenderSystem.setShaderTexture(0, STATION_TAB_BASE_ID);
                }
                drawTexture(
                        matrices,
                        intoTexturePosX(x0),
                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0),
                        0,
                        0,
                        STATION_TAB_BASE_WIDTH, STATION_TAB_BASE_HEIGHT,
                        STATION_TAB_BASE_WIDTH, STATION_TAB_BASE_HEIGHT
                );
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

//                // Station color
//                float r = ColorHelper.Argb.getRed(station.color);
//                float g = ColorHelper.Argb.getGreen(station.color);
//                float b = ColorHelper.Argb.getBlue(station.color);
//                RenderSystem.setShaderColor(r / 256f, g / 256f, b / 255f, 1f);
//                RenderSystem.setShaderTexture(0, new Identifier(Metropolis.MOD_ID, "textures/blanco.png"));
//                drawTexture(
//                        matrices,
//                        intoTexturePosX(x0 + 4),
//                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 4),
//                        0,
//                        0,
//                        8, 8,
//                        8, 8
//                );
//                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                // Station name
                matrices.push();
                matrices.scale(scaleFactor, scaleFactor, scaleFactor);
                String stationName = station.name;
                String[] arr = station.name.split("\\|");
                if (arr.length > 1) {
                    stationName = arr[0] + " " + arr[1];
                }
                int offset = (int) (this.client.world.getTime() / 5) % (stationName.length() + 1);
                if (textRenderer.getWidth(stationName) * scaleFactor > maxStrWidth) {
                    String str0 = stationName.substring(offset) + " " + stationName;
                    int var0 = str0.length();
                    while (textRenderer.getWidth(str0) * scaleFactor > maxStrWidth) {
                        str0 = str0.substring(0, var0);
                        var0 -= 1;
                    }
                    textRenderer.draw(
                            matrices,
                            str0,
                            intoTexturePosX(x0 + 4) / scaleFactor,
                            intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 6) / scaleFactor,
                            0x3F3F3F
                    );
//                    VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
//                    this.textRenderer.drawWithOutline(
//                            Text.literal(str0).asOrderedText(),
//                            intoTexturePosX(x0 + 4) / scaleFactor,
//                            intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
//                            0xFFFFFF,
//                            station.color,
//                            matrices.peek().getPositionMatrix(),
//                            immediate,
//                            15728880
//                    );
//                    immediate.draw();
                } else {
//                    VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
//                    this.textRenderer.drawWithOutline(
//                            Text.literal(stationName).asOrderedText(),
//                            intoTexturePosX(x0 + 4) / scaleFactor,
//                            intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
//                            0xFFFFFF,
//                            station.color,
//                            matrices.peek().getPositionMatrix(),
//                            immediate,
//                            15728880
//                    );
//                    immediate.draw();
                    textRenderer.draw(
                            matrices,
                            stationName,
                            intoTexturePosX(x0 + 4) / scaleFactor,
                            intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 6) / scaleFactor,
                            0x3F3F3F
                    );
                }

                // Station right arrow
                textRenderer.draw(
                        matrices,
                        Text.literal(">"),
                        intoTexturePosX(x0 + STATION_TAB_BASE_WIDTH - 8) / scaleFactor,
                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
                        0x3F3F3F
                );

                matrices.pop();

                i0++;
            }
        }

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
                intoTexturePosX(24),
                intoTexturePosY(35),
                0x3F3F3F
        );

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
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        System.out.println(amount);
        sliderPos -= amount;
//        if (stations.size() - MAX_VISIBLE > (sliderPos - amount)) {
//            sliderPos = stations.size() - MAX_VISIBLE;
//        } else {
//            sliderPos += amount;
//        }
        sliderPos = Math.min(Math.max(0, sliderPos), stations.size() - MAX_VISIBLE);
        System.out.println(sliderPos);
        return super.mouseScrolled(mouseX, mouseY, amount);
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
