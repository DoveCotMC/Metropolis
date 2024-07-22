package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.item.MetroItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorScreen3 extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/ticket_vendor_3_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final Identifier BUTTON_UPPER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_upper.png");
    private static final Identifier BUTTON_UPPER_TEXTURE_HOVER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_upper_hover.png");
    protected static final int BUTTON_UPPER_TEXTURE_WIDTH = 64;
    protected static final int BUTTON_UPPER_TEXTURE_HEIGHT = 14;

    private static final Identifier BUTTON_CONTINUE_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_continue.png");
    private static final Identifier BUTTON_CONTINUE_TEXTURE_HOVER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_continue_hover.png");
    protected static final int BUTTON_CONTINUE_TEXTURE_WIDTH = 60;
    protected static final int BUTTON_CONTINUE_TEXTURE_HEIGHT = 16;

    private static final Identifier BUTTON_NUMBER_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_number.png");
    private static final Identifier BUTTON_NUMBER_TEXTURE_HOVER_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_number_hover.png");
    private static final Identifier BUTTON_NUMBER_TEXTURE_DOWN_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor_3/button_number_down.png");
    protected static final int BUTTON_NUMBER_TEXTURE_WIDTH = 18;
    protected static final int BUTTON_NUMBER_TEXTURE_HEIGHT = 14;

    protected final Screen parentScreen;
    protected final BlockPos pos;
    public final List<Integer> inputToHandle;
    public final List<Integer> inputToHandle2;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = true;
    private boolean lastPressing = true;
    protected boolean pressed = false;
    protected String value = "0";

    public TicketVendorScreen3(BlockPos pos, Screen parentScreen, TicketVendorData data) {
        super(Text.translatable("gui.metropolis.ticket_vendor_3.title"));
        this.pos = pos;
        this.parentScreen = parentScreen;
        this.inputToHandle = new ArrayList<>();
        this.inputToHandle2 = new ArrayList<>();
    }

    @Override
    protected void init() {
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);
//        renderTooltip();

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

        float scaleFactor = 8f / this.textRenderer.fontHeight;

        // Given prices
        int x0 = 31;
        int y0 = 35;
        int i0 = 0;
        // TODO: Configurable
        int[] givenPrices = {1, 2, 4, 8, 16, 24, 32, 48, 64};
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                int x1a = intoTexturePosX(x0 + (BUTTON_UPPER_TEXTURE_WIDTH + 1) * i);
                int y1a = intoTexturePosY(y0 + (BUTTON_UPPER_TEXTURE_HEIGHT + 2) * j);
                boolean hovering = mouseX >= x1a && mouseY >= y1a && mouseX < x1a + BUTTON_CONTINUE_TEXTURE_WIDTH && mouseY < y1a + BUTTON_CONTINUE_TEXTURE_HEIGHT;
                if (hovering) {
                    RenderSystem.setShaderTexture(0, BUTTON_UPPER_TEXTURE_HOVER_ID);
                } else {
                    RenderSystem.setShaderTexture(0, BUTTON_UPPER_TEXTURE_ID);
                    RenderSystem.setShaderColor(96f / 256f, 96f / 256f, 96f / 256f, 1f);
                }

                drawTexture(
                        matrices,
                        intoTexturePosX(x0 + (BUTTON_UPPER_TEXTURE_WIDTH + 1) * i),
                        intoTexturePosY(y0 + (BUTTON_UPPER_TEXTURE_HEIGHT + 2) * j),
                        0,
                        0,
                        BUTTON_UPPER_TEXTURE_WIDTH, BUTTON_UPPER_TEXTURE_HEIGHT,
                        BUTTON_UPPER_TEXTURE_WIDTH, BUTTON_UPPER_TEXTURE_HEIGHT
                );
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                matrices.push();
                matrices.scale(scaleFactor, scaleFactor, scaleFactor);
                Text price = Text.translatable("misc.metropolis.cost", String.valueOf(givenPrices[i0]));
                textRenderer.draw(
                        matrices,
                        price,
                        intoTexturePosX(x0 + (BUTTON_UPPER_TEXTURE_WIDTH + 1) * i + (BUTTON_UPPER_TEXTURE_WIDTH / 2f - textRenderer.getWidth(price) * scaleFactor / 2f)) / scaleFactor,
                        intoTexturePosY(y0 + (BUTTON_UPPER_TEXTURE_HEIGHT + 2) * j + (BUTTON_UPPER_TEXTURE_HEIGHT / 2f - textRenderer.fontHeight * scaleFactor / 2f) + 1) / scaleFactor,
                        0xFFFFFF
                );
                matrices.pop();

                if (hovering && pressed) {
                    playButtonSound(MinecraftClient.getInstance().getSoundManager());
                    this.value = String.valueOf(givenPrices[i0]);
                }

                i0++;
            }
        }

        // Continue button
        int x1 = 165;
        int y1 = 85;
        boolean continueHovering = mouseX >= intoTexturePosX(x1) && mouseY >= intoTexturePosY(y1) && mouseX < intoTexturePosX(x1) + BUTTON_CONTINUE_TEXTURE_WIDTH && mouseY < intoTexturePosY(y1) + BUTTON_CONTINUE_TEXTURE_HEIGHT;
        if (continueHovering) {
            RenderSystem.setShaderTexture(0, BUTTON_CONTINUE_TEXTURE_HOVER_ID);
        } else {
            RenderSystem.setShaderTexture(0, BUTTON_CONTINUE_TEXTURE_ID);
            RenderSystem.setShaderColor(241f / 256f, 175f / 256f, 21f / 256f, 1f);
        }
        drawTexture(
                matrices,
                intoTexturePosX(x1),
                intoTexturePosY(y1),
                0,
                0,
                BUTTON_CONTINUE_TEXTURE_WIDTH, BUTTON_CONTINUE_TEXTURE_HEIGHT,
                BUTTON_CONTINUE_TEXTURE_WIDTH, BUTTON_CONTINUE_TEXTURE_HEIGHT
        );
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Text continueText = Text.translatable("gui.metropolis.ticket_vendor_3.continue");
        textRenderer.draw(
                matrices,
                continueText,
                intoTexturePosX(x1 + (BUTTON_CONTINUE_TEXTURE_WIDTH / 2f - textRenderer.getWidth(continueText) * scaleFactor / 2f)) / scaleFactor,
                intoTexturePosY(y1 + (BUTTON_CONTINUE_TEXTURE_HEIGHT / 2f - textRenderer.fontHeight * scaleFactor / 2f) + 1) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();

        if (continueHovering && pressed) {
            playButtonSound(MinecraftClient.getInstance().getSoundManager());

            ItemStack ticketStack = new ItemStack(MetroItems.ITEM_SINGLE_TRIP_TICKET);
            NbtCompound nbt = ticketStack.getOrCreateNbt();
            int cost = Integer.parseInt(value);
            nbt.putInt(ItemTicket.BALANCE, cost);

            if (this.client != null) {
                this.client.setScreen(new TicketVendorPaymentScreen(
                        pos,
                        new TicketVendorPaymentData(
                                TicketVendorPaymentData.EnumTicketVendorPaymentType.SINGLE_TRIP,
                                cost,
                                new Text[] {
                                        Text.translatable("gui.metropolis.ticket_vendor_payment.single_trip.title"),
                                        Text.translatable("gui.metropolis.ticket_vendor_payment.single_trip.ticket_value", cost),
                                        Text.translatable("gui.metropolis.ticket_vendor_payment.single_trip.amount", 1),
                                },
                                ticketStack
                        ),
                        this
                ));
            }
        }

//        scaleFactor = 8f / this.textRenderer.fontHeight;

        // Numbers
        int x2 = 32;
        int y2 = 128;
        int i1 = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                boolean thisHover =
                        mouseX >= intoTexturePosX(x2 + (BUTTON_NUMBER_TEXTURE_WIDTH) * j) &&
                                mouseY >= intoTexturePosY(y2 + (BUTTON_NUMBER_TEXTURE_HEIGHT) * i) &&
                                mouseX < intoTexturePosX(x2 + (BUTTON_NUMBER_TEXTURE_WIDTH) * j + BUTTON_NUMBER_TEXTURE_WIDTH) &&
                                mouseY < intoTexturePosY(y2 + (BUTTON_NUMBER_TEXTURE_HEIGHT) * i + BUTTON_NUMBER_TEXTURE_HEIGHT);
                if (thisHover) {
                    if (pressing) {
                        RenderSystem.setShaderTexture(0, BUTTON_NUMBER_TEXTURE_DOWN_ID);
                    } else {
                        RenderSystem.setShaderTexture(0, BUTTON_NUMBER_TEXTURE_HOVER_ID);
                    }
                } else {
                    RenderSystem.setShaderTexture(0, BUTTON_NUMBER_TEXTURE_ID);
                }

                if (i1 <= 9 && inputToHandle2.contains(i1)) {
                    RenderSystem.setShaderTexture(0, BUTTON_NUMBER_TEXTURE_DOWN_ID);
                } else if (i1 == 11 && inputToHandle2.contains(0)) {
                    RenderSystem.setShaderTexture(0, BUTTON_NUMBER_TEXTURE_DOWN_ID);
                }

                drawTexture(
                        matrices,
                        intoTexturePosX(x2 + (BUTTON_NUMBER_TEXTURE_WIDTH) * j),
                        intoTexturePosY(y2 + (BUTTON_NUMBER_TEXTURE_HEIGHT) * i),
                        0,
                        0,
                        BUTTON_NUMBER_TEXTURE_WIDTH, BUTTON_NUMBER_TEXTURE_HEIGHT,
                        BUTTON_NUMBER_TEXTURE_WIDTH, BUTTON_NUMBER_TEXTURE_HEIGHT
                );

                matrices.push();
                matrices.scale(scaleFactor, scaleFactor, scaleFactor);
                Text text = Text.literal(String.valueOf(i1));

                if (i1 == 10) {
                    text = Text.literal("#");
                } else if (i1 == 11) {
                    text = Text.literal("0");
                } else if (i1 == 12) {
                    text = Text.literal("←");
                }

                this.textRenderer.draw(
                        matrices,
                        text,
                        intoTexturePosX((x2 + (BUTTON_NUMBER_TEXTURE_WIDTH) * j + BUTTON_NUMBER_TEXTURE_WIDTH / 2f) - this.textRenderer.getWidth(text) / 2f) / scaleFactor,
                        intoTexturePosY((y2 + (BUTTON_NUMBER_TEXTURE_HEIGHT) * i + BUTTON_NUMBER_TEXTURE_HEIGHT / 2f) - this.textRenderer.fontHeight / 2f + 2) / scaleFactor,
                        0x3F3F3F
                );
                matrices.pop();

                if (thisHover && pressed) {
                    playButtonSound(MinecraftClient.getInstance().getSoundManager());
                    String buffer = this.value;
                    if (Long.parseLong(this.value) < Integer.MAX_VALUE) {
                        if (i1 <= 9) {
                            if (Objects.equals(this.value, "0"))
                                buffer = String.valueOf(i1);
                            else
                                buffer += String.valueOf(i1);
                        } if (i1 == 10) {
                            // TODO: What the hashtag
                        } else if (i1 == 11) {
                            if (!Objects.equals(buffer, "0"))
                                buffer += "0";
                        } else if (i1 == 12) {
                            if (this.value.length() > 1) {
                                buffer = buffer.substring(0, buffer.length() - 1);
                            } else if (this.value.length() == 1) {
                                buffer = "0";
                            }
                        }
                        try {
                            if (!(Long.parseLong(buffer) > Integer.MAX_VALUE)) {
                                this.value = buffer;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                i1++;
            }
        }

        // Keyboard number input
        if (inputToHandle.size() > 0) {
            int k0 = inputToHandle.get(0);
            String buffer = this.value;
            playButtonSound(MinecraftClient.getInstance().getSoundManager());
            if (Objects.equals(buffer, "0"))
                buffer = String.valueOf(k0);
            else
                buffer += String.valueOf(k0);
            try {
                if (!(Long.parseLong(buffer) > Integer.MAX_VALUE)) {
                    this.value = buffer;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Render text
        // Title
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        this.textRenderer.drawWithOutline(
                Text.translatable("gui.metropolis.ticket_vendor_3.title").asOrderedText(),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                0x16161B,
                matrices.peek().getPositionMatrix(),
                immediate,
                15728880
        );
        immediate.draw();

        // Charge Value
        matrices.push();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        this.textRenderer.drawWithShadow(
                matrices,
                Text.translatable("misc.metropolis.cost", this.value),
                intoTexturePosX(38) / scaleFactor,
                intoTexturePosY(90) / scaleFactor,
                0xFFFFFF
        );
        matrices.pop();

        matrices.pop();

        RenderSystem.disableBlend();

        super.render(matrices, mouseX, mouseY, delta);

        if (pressing) {
            pressed = !lastPressing;
        } else {
            pressed = false;
        }
        lastPressing = pressing;
        this.inputToHandle.clear();
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
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        try {
            int value = Integer.parseInt(InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString());
            if (!inputToHandle.contains(value)) {
                inputToHandle.add(value);
            }
            if (!inputToHandle2.contains(value)) {
                inputToHandle2.add(value);
            }
        } catch (NumberFormatException ignored) {}

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        try {
            int value = Integer.parseInt(InputUtil.fromKeyCode(keyCode, scanCode).getLocalizedText().getString());
            if (inputToHandle.contains(value)) {
                inputToHandle.remove((Integer) value);
            }
            if (inputToHandle2.contains(value)) {
                inputToHandle2.remove((Integer) value);
            }
        } catch (NumberFormatException ignored) {}
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parentScreen);
        }
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }

    public void playButtonSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
