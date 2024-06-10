package team.dovecotmc.metropolis.metropolis.client.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityTurnstile;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TurnstileBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTurnstile> {
    public static final EntityModelLayer MODEL_LAYER = new EntityModelLayer(new Identifier(Metropolis.MOD_ID, "turnstile"), "main");
    private final ModelPart modelPart;

    public TurnstileBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.modelPart = ctx.getLayerModelPart(MODEL_LAYER);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("right", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(0.0f, -6.0f, -6.0f, 1.0f, 8.0f, 6.0f), ModelTransform.NONE);
        modelPartData.addChild("left", ModelPartBuilder.create().uv(0, 0).mirrored().cuboid(-1.0f, -6.0f, -6.0f, 1.0f, 8.0f, 6.0f), ModelTransform.NONE);
        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(BlockEntityTurnstile entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        World world = entity.getWorld();

//        ChestBlockEntityRenderer
//        VertexConsumer vertexConsumer = getTexture().getVertexConsumer(vertexConsumers, RenderLayer::getEntityCutout);
//        matrices.push();
//        matrices.translate(7f / 16f, 1, 0);
//        ModelPart partRight = this.modelPart.getChild("right");
//        partRight.pivotX = 6.0F;
//        partRight.pivotY = -6.0F;
//        partRight.pivotZ = 5.0F;
//        partRight.yaw = (float) ((float) Math.toRadians(67.5) + Math.sin(world.getTime() + tickDelta));
//        partRight.render(matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(entity.getCachedState())), light, overlay);
//        matrices.pop();
//
//        matrices.push();
//        matrices.translate(7f / 16f, 1, 0);
//        ModelPart partLeft = this.modelPart.getChild("left");
//        partLeft.pivotX = -6.0F;
//        partLeft.pivotY = -6.0F;
//        partLeft.pivotZ = 5.0F;
//        partLeft.yaw = (float) ((float) Math.toRadians(-67.5) - Math.sin(world.getTime() + tickDelta));
////        partLeft.yaw = (float) Math.toRadians(-67.5);
//        partLeft.render(matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(entity.getCachedState())), light, overlay);
//        matrices.pop();
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

    public static SpriteIdentifier getTexture() {
        // TODO: How to add texture??
        return new SpriteIdentifier(new Identifier(Metropolis.MOD_ID, "entity/turnstile"), new Identifier(Metropolis.MOD_ID, "entity/turnstile"));
    }
}
