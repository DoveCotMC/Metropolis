package team.dovecotmc.metropolis.metropolis.client.block.entity;

import mtr.block.IBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityTicketMachine;
import team.dovecotmc.metropolis.metropolis.item.MetroItems;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Environment(EnvType.CLIENT)
public class TicketMachineBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTicketMachine> {
    @Override
    public void render(BlockEntityTicketMachine entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getCachedState().get(IBlock.HALF) == DoubleBlockHalf.LOWER) {
            return;
        }

        MinecraftClient mc = MinecraftClient.getInstance();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        // Render Ticket
        if (entity.ticketSlotOccupied) {
            matrices.push();
            matrices.translate(
                    0.25 + 0.0625,
                    0.5 - 0.0625,
                    0.3 + Math.pow((1 - Math.min(1, ((mc.world.getTime() + tickDelta) - entity.ticketSlotAnimationTick) / 10d)) * 0.3, 2)
            );
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(-90, 0, 90)));
            matrices.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderItem(new ItemStack(MetroItems.ITEM_TICKET), ModelTransformation.Mode.GROUND, light, 0, matrices, vertexConsumers, 0);
//            itemRenderer.renderItem(entity.getStack(1), ModelTransformation.Mode.GROUND, light, 0, matrices, vertexConsumers, 0);
            matrices.pop();
        }

        // Render Card
        if (entity.cardSlotOccupied) {
            matrices.push();
            matrices.translate(
                    0.25 + 0.0625,
                    0.0625 * 2 + Math.pow((1 - Math.min(1, ((mc.world.getTime() + tickDelta) - entity.ticketSlotAnimationTick) / 2d)), 1.5) * 0.2d,
                    0.0625 * 2
            );
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(
                    67.5f,
                    0,
                    30f + (float) Math.toRadians(Math.pow((1 - Math.min(1, ((mc.world.getTime() + tickDelta) - entity.ticketSlotAnimationTick) / 3d)) * 60, 2))
            )));
            matrices.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, 0, matrices, vertexConsumers, 0);
            matrices.pop();
        }

        float fontSize = 8;
        matrices.push();
        matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(180, 180, 0)));
        matrices.translate(-13 / 16f, -10 / 16f, (5 - 0.01) / 16f);
        matrices.scale(1 / 16f / fontSize, 1 / 16f / fontSize, 1 / 16f / fontSize);

        matrices.scale(0.5f, 0.5f, 0.5f);
        matrices.translate(0, 3, 0);
        mc.textRenderer.draw(Text.translatable("gui.metropolis.info.ticket_machine.0"), 0, 0, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0x00000000, light);

        matrices.pop();
//        context.drawText(client.textRenderer, "Hello, world!", 10, 200, 0xFFFFFFFF, false);
    }


    @Override
    public boolean rendersOutsideBoundingBox(BlockEntityTicketMachine blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityTicketMachine blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
