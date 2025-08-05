package team.dovecotmc.metropolis.client.gui.ticket_vendor;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import mtr.data.Station;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.item.MetroItems;
import team.dovecotmc.metropolis.util.MtrStationUtil;

import java.util.*;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketVendorScreen2 extends Screen {
    private static final ResourceLocation BG_TEXTURE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/ticket_vendor_2_base.png");
    protected static final int BG_TEXTURE_WIDTH = 256;
    protected static final int BG_TEXTURE_HEIGHT = 196;

    private static final ResourceLocation STATION_TAB_BASE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/station_tab_base.png");
    private static final ResourceLocation STATION_TAB_BASE_HOVER_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/station_tab_base_hover.png");
    protected static final int STATION_TAB_BASE_WIDTH = 150;
    protected static final int STATION_TAB_BASE_HEIGHT = 16;

    private static final ResourceLocation VALUE_BUTTON_BASE_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/value_button_base.png");
    private static final ResourceLocation VALUE_BUTTON_BASE_HOVER_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/value_button_base_hover.png");
    protected static final int VALUE_TAB_BASE_WIDTH = 56;
    protected static final int VALUE_TAB_BASE_HEIGHT = 16;


    protected static final int MAX_VISIBLE = 8;

    private static final ResourceLocation SLIDER_ID = new ResourceLocation(Metropolis.MOD_ID, "textures/gui/ticket_vendor_2/slider.png");
    protected static final int SLIDER_WIDTH = 6;
    protected static final int SLIDER_HEIGHT = 9;

    protected final BlockPos pos;
    protected final Screen parentScreen;
    protected final TicketVendorData data;

    protected double mouseX = 0;
    protected double mouseY = 0;
    protected boolean pressing = true;
    private boolean lastPressing = true;
    protected boolean pressed = false;

    protected Set<Station> stations;
    protected int sliderPos = 0;

    protected int tipId = 0;

    public TicketVendorScreen2(BlockPos pos, Screen parentScreen, TicketVendorData data) {
        super(MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_2.title"));
        this.pos = pos;
        this.parentScreen = parentScreen;
        this.data = data;
        if (this.minecraft != null && this.minecraft.level != null) {
            this.stations = MtrStationUtil.getStations(this.minecraft.level);
        } else {
            this.stations = new HashSet<>();
        }
    }

    @Override
    protected void init() {
        if (Minecraft.getInstance().level != null) {
            tipId = Minecraft.getInstance().level.random.nextInt(3);
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.fillGradient(matrices, 0, 0, this.width, this.height, -1072689136, -804253680);

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShaderTexture(0, BG_TEXTURE_ID);
        blit(
                matrices,
                this.width / 2 - BG_TEXTURE_WIDTH / 2,
                this.height / 2 - BG_TEXTURE_HEIGHT / 2,
                0,
                0,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT,
                BG_TEXTURE_WIDTH, BG_TEXTURE_HEIGHT
        );

        // Render text
        // Title
        MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        this.font.drawInBatch8xOutline(
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_2.title").getVisualOrderText(),
                intoTexturePosX(36),
                intoTexturePosY(12),
                0xFFFFFF,
                0x16161B,
                matrices.last().pose(),
                immediate,
                15728880
        );
        immediate.endBatch();

        // Subtitle
        this.font.draw(
                matrices,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_2.subtitle"),
                intoTexturePosX(20),
                intoTexturePosY(35),
                0x3F3F3F
        );

        // Station selection
        float scaleFactor = 12f / 14f;
        if (this.minecraft != null && this.minecraft.level != null) {
            stations = MtrStationUtil.getStations(this.minecraft.level);

            Station locatedStation = MtrStationUtil.getStationByPos(pos, minecraft.level);
            if (locatedStation == null) {
                minecraft.setScreen(new TicketVendorScreen3(pos, this.parentScreen, this.data));
                return;
            }
            int stationsSize = stations.size();

            List<Station> sortedStations = stations.stream().sorted(Comparator.comparingInt(o -> (Math.abs(o.zone - locatedStation.zone)))).toList();

            final int maxStrWidth = 96;
//                int h0 = 128;
            int x0 = 20;
            int y0 = 51;
            int i0 = -sliderPos;

            // Slider1
            int h1 = 119;
            int x1 = 174;
            int y1 = (int) (51 + (float) sliderPos / (float) (stationsSize - MAX_VISIBLE) * h1);

            RenderSystem.setShaderTexture(0, SLIDER_ID);
            blit(
                    matrices,
                    intoTexturePosX(x1),
                    intoTexturePosY(y1),
                    0,
                    0,
                    SLIDER_WIDTH, SLIDER_HEIGHT,
                    SLIDER_WIDTH, SLIDER_HEIGHT
            );

            for (Station station : sortedStations) {
                // Station base
                if (i0 < 0) {
                    i0++;
                    continue;
                }

                if (i0 >= MAX_VISIBLE) {
                    break;
                }

                boolean thisTabHovering = this.mouseX >= intoTexturePosX(x0) && this.mouseY >= intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0) && this.mouseX <= intoTexturePosX(x0 + STATION_TAB_BASE_WIDTH) && this.mouseY <= intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + STATION_TAB_BASE_HEIGHT);
                if (thisTabHovering) {
                    RenderSystem.setShaderTexture(0, STATION_TAB_BASE_HOVER_ID);
                } else {
                    RenderSystem.setShaderTexture(0, STATION_TAB_BASE_ID);
                }
                blit(
                        matrices,
                        intoTexturePosX(x0),
                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0),
                        0,
                        0,
                        STATION_TAB_BASE_WIDTH, STATION_TAB_BASE_HEIGHT,
                        STATION_TAB_BASE_WIDTH, STATION_TAB_BASE_HEIGHT
                );

                // Station color
                float r = FastColor.ARGB32.red(station.color);
                float g = FastColor.ARGB32.green(station.color);
                float b = FastColor.ARGB32.blue(station.color);
                RenderSystem.setShaderColor(r / 256f, g / 256f, b / 255f, 1f);
                RenderSystem.setShaderTexture(0, new ResourceLocation(Metropolis.MOD_ID, "textures/blanco.png"));
                blit(
                        matrices,
                        intoTexturePosX(x0 + 4),
                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 4),
                        0,
                        0,
                        8, 8,
                        8, 8
                );
                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

                // Station name
                matrices.pushPose();
                matrices.scale(scaleFactor, scaleFactor, scaleFactor);
                String stationName = station.name;
                String[] arr0 = station.name.split("\\|");
                if (arr0.length > 1) {
                    stationName = arr0[0] + " " + arr0[1];
                }
                int offset = (int) (this.minecraft.level.getGameTime() / 5) % (stationName.length() + 1);
                if (font.width(stationName) * scaleFactor > maxStrWidth) {
                    String str0 = stationName.substring(offset) + " " + stationName;
                    int var0 = str0.length();
                    while (font.width(str0) * scaleFactor > maxStrWidth) {
                        str0 = str0.substring(0, var0);
                        var0 -= 1;
                    }
                    font.draw(
                            matrices,
                            str0,
                            intoTexturePosX(x0 + 16) / scaleFactor,
                            intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
                            0x3F3F3F
                    );
                } else {
                    font.draw(
                            matrices,
                            stationName,
                            intoTexturePosX(x0 + 16) / scaleFactor,
                            intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
                            0x3F3F3F
                    );
                }

                // Station cost
                int cost = Math.abs(station.zone - locatedStation.zone) + 1;
                Component costText = MALocalizationUtil.translatableText("misc.metropolis.cost", cost);
                font.draw(
                        matrices,
                        costText,
                        intoTexturePosX(x0 + STATION_TAB_BASE_WIDTH - 20 - font.width(costText) / 2f) / scaleFactor,
                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
                        0x3F3F3F
                );

                // Station right arrow
                font.draw(
                        matrices,
                        MALocalizationUtil.literalText(">"),
                        intoTexturePosX(x0 + STATION_TAB_BASE_WIDTH - 8) / scaleFactor,
                        intoTexturePosY(y0 + STATION_TAB_BASE_HEIGHT * i0 + 5) / scaleFactor,
                        0x3F3F3F
                );

                matrices.popPose();

                // Go to payment
                if (thisTabHovering && pressed) {
                    if (this.minecraft.level != null) {
                        playDownSound(Minecraft.getInstance().getSoundManager());
                    }
                    String locatedStationFirstName = station.name;
                    String[] arr1 = locatedStation.name.split("\\|");
                    if (arr1.length > 1) {
                        locatedStationFirstName = arr1[0];
                    }
                    String stationFirstName = station.name;
                    String[] arr2 = station.name.split("\\|");
                    if (arr2.length > 1) {
                        stationFirstName = arr2[0];
                    }

                    ItemStack ticketStack = new ItemStack(MetroItems.ITEM_SINGLE_TRIP_TICKET);
                    CompoundTag nbt = ticketStack.getOrCreateTag();
                    nbt.putInt(ItemTicket.BALANCE, cost);
                    nbt.putString(ItemTicket.START_STATION, locatedStationFirstName);
                    nbt.putString(ItemTicket.END_STATION, stationFirstName);

                    this.minecraft.setScreen(new TicketVendorPaymentScreen(
                            pos,
                            new TicketVendorPaymentData(
                                    TicketVendorPaymentData.EnumTicketVendorPaymentType.SINGLE_TRIP,
                                     cost,
                                    new Component[] {
                                            MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.title"),
                                            MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.from_and_to", locatedStationFirstName, stationFirstName),
                                            MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.ticket_value", cost),
                                            MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.amount", 1),
                                    },
                                    ticketStack
                            ),
                            this
                    ));
                }

                i0++;
            }
        }

        // Right part

        // Subtitle 2
        this.font.draw(
                matrices,
                MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_2.subtitle_2"),
                intoTexturePosX(184),
                intoTexturePosY(35),
                0x3F3F3F
        );

        int x1 = 184;
        int y1 = 51;

        // TODO: Configurable
        int[] valuesToSelect = {1, 2, 3, 4, 5, 6, 7};

        for (int i = 0; i < 7; i++) {
            boolean thisTabHovering = this.mouseX >= intoTexturePosX(x1) && this.mouseY >= intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * i) && this.mouseX <= intoTexturePosX(x1 + VALUE_TAB_BASE_WIDTH) && this.mouseY <= intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * i + VALUE_TAB_BASE_HEIGHT);
            if (thisTabHovering) {
                RenderSystem.setShaderTexture(0, VALUE_BUTTON_BASE_HOVER_ID);
            } else {
                RenderSystem.setShaderColor(96f / 256f, 96f / 256f, 96f / 256f, 1f);
                RenderSystem.setShaderTexture(0, VALUE_BUTTON_BASE_ID);
            }
//            RenderSystem.setShaderTexture(0, VALUE_BUTTON_BASE_ID);
            blit(
                    matrices,
                    intoTexturePosX(x1),
                    intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * i),
                    0,
                    0,
                    VALUE_TAB_BASE_WIDTH, VALUE_TAB_BASE_HEIGHT,
                    VALUE_TAB_BASE_WIDTH, VALUE_TAB_BASE_HEIGHT
            );
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

            matrices.pushPose();
            matrices.scale(scaleFactor, scaleFactor, scaleFactor);
            Component cost = MALocalizationUtil.literalText(MALocalizationUtil.translatableText("misc.metropolis.cost", valuesToSelect[i]).getString());
            font.draw(
                    matrices,
                    cost,
                    intoTexturePosX(x1 + VALUE_TAB_BASE_WIDTH / 2f - font.width(cost) / 2f) / scaleFactor,
                    intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * i + 5) / scaleFactor,
                    0xFFFFFF
            );
            matrices.popPose();

            // Go to payment
            if (thisTabHovering && pressed) {
                if (this.minecraft.level != null) {
                    playDownSound(Minecraft.getInstance().getSoundManager());
                }

                ItemStack ticketStack = new ItemStack(MetroItems.ITEM_SINGLE_TRIP_TICKET);
                CompoundTag nbt = ticketStack.getOrCreateTag();
                nbt.putInt(ItemTicket.BALANCE, i + 1);

                this.minecraft.setScreen(new TicketVendorPaymentScreen(
                        pos,
                        new TicketVendorPaymentData(
                                TicketVendorPaymentData.EnumTicketVendorPaymentType.SINGLE_TRIP,
                                i + 1,
                                new Component[] {
                                        MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.title"),
                                        MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.ticket_value", MALocalizationUtil.translatableText("misc.metropolis.cost", i + 1).getString()),
                                        MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_payment.single_trip.amount", 1),
                                },
                                ticketStack
                        ),
                        this
                ));
            }
        }

        // Custom button
        boolean customHovering = this.mouseX >= intoTexturePosX(x1) && this.mouseY >= intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * 7) && this.mouseX <= intoTexturePosX(x1 + VALUE_TAB_BASE_WIDTH) && this.mouseY <= intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * 7 + VALUE_TAB_BASE_HEIGHT);
        if (customHovering) {
            RenderSystem.setShaderTexture(0, VALUE_BUTTON_BASE_HOVER_ID);
        } else {
            RenderSystem.setShaderColor(61f / 256f, 169f / 256f, 58f / 256f, 1f);
            RenderSystem.setShaderTexture(0, VALUE_BUTTON_BASE_ID);
        }
        blit(
                matrices,
                intoTexturePosX(x1),
                intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * 7),
                0,
                0,
                VALUE_TAB_BASE_WIDTH, VALUE_TAB_BASE_HEIGHT,
                VALUE_TAB_BASE_WIDTH, VALUE_TAB_BASE_HEIGHT
        );
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        matrices.pushPose();
        matrices.scale(scaleFactor, scaleFactor, scaleFactor);
        Component moreCostOptions = MALocalizationUtil.translatableText("gui.metropolis.ticket_vendor_2.custom_value_option");
        font.draw(
                matrices,
                moreCostOptions,
                intoTexturePosX(x1 + VALUE_TAB_BASE_WIDTH / 2f - font.width(moreCostOptions) / 2f) / scaleFactor,
                intoTexturePosY(y1 + VALUE_TAB_BASE_HEIGHT * 7 + 5) / scaleFactor,
                0xFFFFFF
        );
        matrices.popPose();

        if (customHovering && pressed) {
            playDownSound(this.minecraft.getSoundManager());
            this.minecraft.setScreen(new TicketVendorScreen3(pos, this, this.data));
        }

        RenderSystem.disableBlend();

        super.render(matrices, mouseX, mouseY, delta);

        if (pressing) {
            pressed = !lastPressing;
        } else {
            pressed = false;
        }
        lastPressing = pressing;
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
        sliderPos -= amount;
        sliderPos = Math.min(Math.max(0, sliderPos), stations.size() - MAX_VISIBLE);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(new TicketVendorScreen1(pos, this.data));
        }
    }

    private int intoTexturePosX(double x) {
        return (int) (this.width / 2 - BG_TEXTURE_WIDTH / 2 + x);
    }

    private int intoTexturePosY(double y) {
        return (int) (this.height / 2 - BG_TEXTURE_HEIGHT / 2 + y);
    }

    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
