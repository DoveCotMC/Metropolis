package team.dovecotmc.metropolis.metropolis.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsSettingsScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketMachineScreen extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_machine.png");
    protected static int IMAGE_WIDTH = 256;
    protected static int IMAGE_HEIGHT = 196;
    public ItemStack ticketItem;

    public TicketMachineScreen(ItemStack ticketItem) {
        super(Text.translatable("metropolis.title.screen.ticket_machine"));
        this.ticketItem = ticketItem;
    }

    @Override
    protected void init() {
        ButtonWidget buttonTest = new ButtonWidget(0, 0, 128, 20, Text.translatable("metropolis.screen.ticket_machine.button.test"), button -> {
            NbtCompound nbt = this.ticketItem.getOrCreateNbt();
            nbt.putInt(ItemTicket.REMAIN_MONEY, nbt.getInt(ItemTicket.REMAIN_MONEY) + 1);
        });
        addDrawableChild(buttonTest);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, BG_TEXTURE_ID);
        drawTexture(matrices, this.width / 2 - IMAGE_WIDTH / 2, this.height / 2 - IMAGE_HEIGHT / 2, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
        RenderSystem.disableBlend();

        super.render(matrices, mouseX, mouseY, delta);

        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(8.0, 8.0, 0.0);
        matrixStack.scale(1.0F, -1.0F, 1.0F);
        matrixStack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();

        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        itemRenderer.renderItem(this.ticketItem, ModelTransformation.Mode.GROUND, 15728880, OverlayTexture.DEFAULT_UV, matrices, immediate, 0);

        matrixStack.pop();
//        RenderSystem.applyModelViewMatrix();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        super.close();
        ClientPlayNetworking.send(Metropolis.ID_SCREEN_CLOSE_TICKET_MACHINE, PacketByteBufs.create().writeItemStack(this.ticketItem));
    }
}
