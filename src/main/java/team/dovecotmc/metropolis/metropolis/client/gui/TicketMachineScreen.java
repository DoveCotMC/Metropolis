package team.dovecotmc.metropolis.metropolis.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.realms.gui.screen.RealmsSettingsScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import team.dovecotmc.metropolis.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TicketMachineScreen extends Screen {
    private static final Identifier BG_TEXTURE_ID = new Identifier(Metropolis.MOD_ID, "textures/gui/ticket_machine.png");
    protected static int imageWidth = 176;
    protected static int imageHeight = 196;
    public ItemStack ticketItem;

    public TicketMachineScreen(ItemStack ticketItem) {
        super(Text.translatable("metropolis.title.screen.ticket_machine"));
        this.ticketItem = ticketItem;
    }

    @Override
    protected void init() {
        ButtonWidget buttonTest = new ButtonWidget(0, 0, 16, 16, Text.translatable("metropolis.screen.ticket_machine.button.test"), button -> {
            System.out.println("baka");
        });
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.assertOnRenderThread();
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0, BG_TEXTURE_ID);
        drawTexture(matrices, 0, 0, 0, 0, this.width, this.height);
        RenderSystem.disableBlend();

//        itemRenderer.renderItem(this.ticketItem, ModelTransformation.Mode.GROUND, false, matrices, VertexConsumers.union(), 1, 0);
//        itemRenderer.renderItem(this.ticketItem, ModelTransformation.Mode.GROUND, 1, 1, matrices, 1);
        itemRenderer.renderInGui(this.ticketItem, 1, 1);

        super.render(matrices, mouseX, mouseY, delta);
    }
}
