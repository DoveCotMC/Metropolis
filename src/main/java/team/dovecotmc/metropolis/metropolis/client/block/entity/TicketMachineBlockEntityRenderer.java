package team.dovecotmc.metropolis.metropolis.client.block.entity;

import mtr.block.IBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityTicketMachine;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;
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
        Text textInfoHovering = null;

        if (entity.ticketSellingMode) {
            textInfoHovering = Text.literal(String.format(Text.translatable("gui.metropolis.info.ticket_machine.ticket_selling").toString(), entity.createNbt().getString(BlockEntityTicketMachine.TAG_EMERALD_CACHE)));
        } else if (entity.ticketSlotOccupied) {
            textInfoHovering = Text.translatable("gui.metropolis.info.ticket_machine.take_ticket");
        } else if (entity.cardSlotOccupied) {
            textInfoHovering = Text.literal(String.format(Text.translatable("gui.metropolis.info.ticket_machine.card_info").getString(), entity.getStack(0).getOrCreateNbt().getInt(ItemTicket.REMAIN_MONEY)));
        } else {
            textInfoHovering = Text.translatable("gui.metropolis.info.ticket_machine.default");
        }

        Vec3d pointingPos = mc.player.raycast(mc.player.isCreative() ? 5 : 4.5, tickDelta, false).getPos();
        BlockPos entityPos = entity.getPos();
        if (
                (int) pointingPos.x == entityPos.getX() &&
                        (int) pointingPos.y == entityPos.getY() &&
                        (int) pointingPos.z == entityPos.getZ()) {
            matrices.push();

            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(180, 180, 0)));
            matrices.translate(-8 / 16f, -4 / 16f, 1 / 16f);
            matrices.scale(1 / 16f / fontSize, 1 / 16f / fontSize, 1 / 16f / fontSize);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, mc.player.getRotationClient().y, 0)));
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(-mc.player.getRotationClient().x, 0, 0)));

            matrices.translate(-mc.textRenderer.getWidth(textInfoHovering) / 2f, 0, 0);
            mc.textRenderer.draw(textInfoHovering, 0, 0, 0xFFFFFFFF, false, matrices.peek().getPositionMatrix(), vertexConsumers, false, 0x00000000, light);

            matrices.pop();
        }

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
