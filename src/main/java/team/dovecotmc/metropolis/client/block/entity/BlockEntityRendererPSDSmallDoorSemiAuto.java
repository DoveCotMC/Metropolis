package team.dovecotmc.metropolis.client.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.block.BlockPSDSmallDoorSemiAuto;
import team.dovecotmc.metropolis.block.entity.BlockEntityPSDSmallDoorSemiAuto;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockEntityRendererPSDSmallDoorSemiAuto implements BlockEntityRenderer<BlockEntityPSDSmallDoorSemiAuto> {
    public static final int DURATION = 40;

    @Override
    public void render(BlockEntityPSDSmallDoorSemiAuto entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient mc = MinecraftClient.getInstance();
        World world = entity.getWorld();

        matrices.push();
        if (world != null) {
            BlockState block = entity.getCachedState();
            Direction facing = block.get(HorizontalFacingBlock.FACING);

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
            matrices.translate(8f, 8f, 8f);
            matrices.multiply(Quaternion.fromEulerXyzDegrees(new Vec3f(0, -facing.asRotation() - 180, 0)));
            matrices.translate(-8f, -8f, -8f);
            matrices.scale(16f, 16f, 16f);

//            double animTime = easeInOutQuad(Math.min(DURATION, (world.getTime() - entity.animationStartTime) + tickDelta) / (double) DURATION);
            double animTime = easeInOutSine(Math.min(DURATION, (world.getTime() - entity.animationStartTime) + tickDelta) / (double) DURATION);
//            double animTime = easeOutBounce(Math.min(DURATION, (world.getTime() - entity.animationStartTime) + tickDelta) / (double) DURATION);
//            System.out.println("---------------------------------------");
//            System.out.println(entity.animationStartTime);
//            System.out.println(entity.createNbt());

            BlockState state = entity.getCachedState();

            if (state.get(BlockPSDSmallDoorSemiAuto.OPEN)) {
                matrices.translate(animTime, 0f, 0f);
            } else {
                matrices.translate(1 - animTime, 0f, 0f);
            }

            BakedModel model = mc.getBlockRenderManager().getModel(state);
            mc.getBlockRenderManager().getModelRenderer().renderSmooth(entity.getWorld(), model, state, entity.getPos(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, entity.getWorld().getRandom(), 0, overlay);
        }
        matrices.pop();
    }

    // https://easings.net/#easeInOutQuad
    private static double easeInOutQuad(double x) {
        return x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2;
    }

    // https://easings.net/#easeOutBounce
    private static final double n1 = 7.5625;
    private static final double d1 = 2.75;

    private static double easeOutBounce(double x) {
        if (x < 1 / d1) {
            return n1 * x * x;
        } else if (x < 2 / d1) {
            return n1 * (x -= 1.5 / d1) * x + 0.75;
        } else if (x < 2.5 / d1) {
            return n1 * (x -= 2.25 / d1) * x + 0.9375;
        } else {
            return n1 * (x -= 2.625 / d1) * x + 0.984375;
        }
    }

    // https://easings.net/#easeInOutSine
    private static double easeInOutSine(double x) {
        return -(Math.cos(Math.PI * x) - 1) / 2;
    }
}
