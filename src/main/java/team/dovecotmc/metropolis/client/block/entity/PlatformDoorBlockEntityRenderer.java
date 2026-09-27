package team.dovecotmc.metropolis.client.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformDoor;
import team.dovecotmc.metropolis.mixins.accessor.AccessorBlockRenderDispatcher;

public class PlatformDoorBlockEntityRenderer implements BlockEntityRenderer<BlockEntityPlatformDoor> {
    @Override
    public void render(BlockEntityPlatformDoor blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
        Minecraft mc = Minecraft.getInstance();
        BlockState state = blockEntity.getBlockState();

        float openValue = blockEntity.getOpenValue(blockEntity.getLevel(), f);
        if (!state.getValue(AbstractBlockPlatformDoor.OPEN)) {
            openValue = (float) Math.pow(1f - openValue, 1);
        }
        float offset = openValue * 14.5f / 16f;

        if (state.getValue(AbstractBlockPlatformDoor.FLIPPED))
            offset *= -1;

        Direction facing = state.getValue(AbstractBlockPlatformDoor.FACING);
        float yRotRad = facing.toYRot() * Mth.DEG_TO_RAD;

        poseStack.pushPose();
        poseStack.mulPoseMatrix(
                new Matrix4f()
                        .translate(-Mth.cos(yRotRad) * offset, 0f, -Mth.sin(yRotRad) * offset)
        );
//        mc.getBlockRenderer().renderSingleBlock(
//                state,
//                poseStack,
//                multiBufferSource,
//                i,
//                j
//        );

        BakedModel bakedModel = mc.getBlockRenderer().getBlockModel(state);
        int k = ((AccessorBlockRenderDispatcher) mc.getBlockRenderer()).getBlockColors().getColor(state, blockEntity.getLevel(), blockEntity.getBlockPos(), i);
        float f0 = (float)(k >> 16 & 0xFF) / 255.0f;
        float g0 = (float)(k >> 8 & 0xFF) / 255.0f;
        float h0 = (float)(k & 0xFF) / 255.0f;
        mc.getBlockRenderer().getModelRenderer().renderModel(poseStack.last(), multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, bakedModel, f0, g0, h0, i, j);

        poseStack.popPose();
    }
}
