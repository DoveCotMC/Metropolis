package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
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
    public void render(BlockEntityPSDSmallDoorSemiAuto entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft mc = Minecraft.getInstance();
        Level world = entity.getLevel();

        matrices.pushPose();
        if (world != null) {
            BlockState block = entity.getBlockState();
            Direction facing = block.getValue(HorizontalDirectionalBlock.FACING);

            matrices.scale(1f / 16f, 1f / 16f, 1f / 16f);
            matrices.translate(8f, 8f, 8f);
            matrices.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, -facing.toYRot() - 180, 0)));
            matrices.translate(-8f, -8f, -8f);
            matrices.scale(16f, 16f, 16f);

            BlockState state = entity.getBlockState();

            double animTime = easeInOutSine(Math.min(DURATION, (world.getGameTime() - entity.animationStartTime) + tickDelta) / (double) DURATION);
            int direction = state.getValue(BlockPSDSmallDoorSemiAuto.FLIPPED) ? -1 : 1;

            if (state.getValue(BlockPSDSmallDoorSemiAuto.OPEN)) {
                matrices.translate(animTime * (14.5f / 16f) * direction, 0f, 0f);
            } else {
                matrices.translate((1 - animTime) * (14.5f / 16f) * direction, 0f, 0f);
            }

            BakedModel model = mc.getBlockRenderer().getBlockModel(state);

            mc.getBlockRenderer().getModelRenderer().tesselateWithAO(entity.getLevel(), model, state, entity.getBlockPos(), matrices, vertexConsumers.getBuffer(ItemBlockRenderTypes.getChunkRenderType(state)), false, entity.getLevel().getRandom(), 0, overlay);
        }
        matrices.popPose();
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
