package team.dovecotmc.metropolis.client.block.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;
import team.dovecotmc.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TurnstileBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTurnstile> {
    public TurnstileBlockEntityRenderer() {
    }

    @Override
    public void render(BlockEntityTurnstile entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        World world = entity.getWorld();
        ItemRenderer itemRenderer = mc.getItemRenderer();

        matrices.push();
        if (!entity.getStack(0).isEmpty()) {
            matrices.scale(4, 4, 4);
            itemRenderer.renderItem(
                    entity.getStack(0),
                    ModelTransformation.Mode.GROUND,
                    light,
                    overlay,
                    matrices,
                    vertexConsumers,
                    0
            );
        }
        matrices.pop();
    }

    @Override
    public boolean rendersOutsideBoundingBox(BlockEntityTurnstile blockEntity) {
        return BlockEntityRenderer.super.rendersOutsideBoundingBox(blockEntity);
    }

    @Override
    public int getRenderDistance() {
        return BlockEntityRenderer.super.getRenderDistance();
    }

    @Override
    public boolean isInRenderDistance(BlockEntityTurnstile blockEntity, Vec3d pos) {
        return BlockEntityRenderer.super.isInRenderDistance(blockEntity, pos);
    }
}
