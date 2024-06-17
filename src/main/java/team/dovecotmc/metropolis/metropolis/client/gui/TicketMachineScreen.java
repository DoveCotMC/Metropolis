package team.dovecotmc.metropolis.metropolis.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.realms.gui.screen.RealmsSettingsScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;
import team.dovecotmc.metropolis.metropolis.item.MetroItems;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketMachineScreen extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_vendor.png");
    protected static int IMAGE_WIDTH = 256;
    protected static int IMAGE_HEIGHT = 196;
//    public ItemStack ticketItem;

    public TicketMachineScreen(BlockPos pos, ItemStack ticket) {
        super(Text.translatable("metropolis.title.screen.ticket_vendor"));
//        this.ticketItem = ticketItem;
    }

    @Override
    protected void init() {
        ButtonWidget buttonTest = new ButtonWidget(0, 0, 128, 20, Text.translatable("metropolis.screen.ticket_machine.button.test"), button -> {
//            NbtCompound nbt = this.ticketItem.getOrCreateNbt();
//            nbt.putInt(ItemTicket.REMAIN_MONEY, nbt.getInt(ItemTicket.REMAIN_MONEY) + 1);
            System.out.println(114514);
        });
        addDrawableChild(buttonTest);
        SliderWidget sliderWidget = new SliderWidget(0, 20, 128, 20, Text.literal("wow"), 0.5) {
            @Override
            protected void updateMessage() {
            }

            @Override
            protected void applyValue() {

            }
        };
        addDrawableChild(sliderWidget);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, BG_TEXTURE_ID);
        drawTexture(matrices, this.width / 2 - IMAGE_WIDTH / 2, this.height / 2 - IMAGE_HEIGHT / 2, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
        RenderSystem.disableBlend();

        super.render(matrices, mouseX, mouseY, delta);

        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        RenderSystem.applyModelViewMatrix();
        matrixStack.push();

        float time = 0;
        if (mc.world != null) {
            time = mc.world.getTime();
        }

        matrixStack.translate(0f, MathHelper.sin((time + mc.getTickDelta()) / 4f) * 4f, 0f);
        int scaleFactor = 4;
        matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);

        ItemStack stack = new ItemStack(MetroItems.ITEM_TICKET);
        itemRenderer.renderInGui(stack, (width / 2 - 16 * scaleFactor / 2) / scaleFactor, (height / 2 - 16 * scaleFactor / 2) / scaleFactor);

        itemRenderer.renderGuiItemOverlay(textRenderer, stack, (width / 2 - 16 * scaleFactor / 2) / scaleFactor, (height / 2 - 16 * scaleFactor / 2) / scaleFactor);

        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
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
}
