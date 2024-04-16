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
            matrices.translate(0.25 + 0.0625, 0.5 - 0.0625, 0.3);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(-90, 0, 90)));
            matrices.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderItem(new ItemStack(MetroItems.ITEM_TICKET), ModelTransformation.Mode.GROUND, light, 0, matrices, vertexConsumers, 0);
//            itemRenderer.renderItem(entity.getStack(1), ModelTransformation.Mode.GROUND, light, 0, matrices, vertexConsumers, 0);
            matrices.pop();
        }

        // Render Card
        if (entity.cardSlotOccupied) {
            matrices.push();
            matrices.translate(0.25 + 0.0625, 0.0625 * 2, 0.0625 * 2);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(67.5f, 0, 30f)));
            matrices.scale(0.5f, 0.5f, 0.5f);
            itemRenderer.renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, 0, matrices, vertexConsumers, 0);
            matrices.pop();
        }
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
