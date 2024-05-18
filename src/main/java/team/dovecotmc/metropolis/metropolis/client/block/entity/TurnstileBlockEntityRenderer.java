package team.dovecotmc.metropolis.metropolis.client.block.entity;

import net.fabricmc.fabric.impl.client.model.ModelLoadingRegistryImpl;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityTurnstile;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class TurnstileBlockEntityRenderer implements BlockEntityRenderer<BlockEntityTurnstile> {
    @Override
    public void render(BlockEntityTurnstile entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        System.out.println(114514);
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = mc.getBlockRenderManager();
        BlockModelRenderer blockModelRenderer = blockRenderManager.getModelRenderer();
//        blockRenderManager.getModels().getModelManager().getBlockModels();
//        blockRenderManager.getModels().getModelManager().getModel(new ModelIdentifier("metropolis:block/turnstile_r#inventory"));
        System.out.println(blockRenderManager.getModels().getModel(entity.getCachedState()));
//        BakedModel baseModelRight = blockRenderManager.getModels().getModelManager().getModel(new ModelIdentifier("metropolis:block/turnstile_r#inventory"));
//        BakedModel baseModelRight = blockRenderManager.getModel(entity.getCachedState());
//        BakedModel baseModelRight = blockRenderManager.getModels().getModelManager().getMissingModel();
        BakedModel baseModelRight = blockRenderManager.getModels().getModel(entity.getCachedState());
//        System.out.println(new ModelIdentifier(Metropolis.MOD_ID, "block/turnstile_r", ""));
        blockModelRenderer.render(
                entity.getWorld(),
                baseModelRight,
                entity.getCachedState(),
                entity.getPos(),
                matrices,
                vertexConsumers.getBuffer(RenderLayers.getBlockLayer(entity.getCachedState())),
                false,
                entity.getWorld().getRandom(),
                light,
                overlay
        );

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
