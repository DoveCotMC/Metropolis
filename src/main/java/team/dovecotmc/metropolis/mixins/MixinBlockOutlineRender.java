package team.dovecotmc.metropolis.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.Station;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.dovecotmc.metropolis.client.MetropolisClient;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Mixin(WorldRenderer.class)
public abstract class MixinBlockOutlineRender {
    @Shadow
    private static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {}

    @Shadow @Nullable private ClientWorld world;

    @Inject(at = @At("TAIL"), method = "drawBlockOutline")
    public void renderTail(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (MetropolisClient.BLOCK_PLACE_HUD.shouldRender && world != null) {
            Station station = MtrStationUtil.getStationByPos(pos, world);
            if (station != null) {
                float red = ColorHelper.Argb.getRed(station.color) / 255f;
                float green = ColorHelper.Argb.getGreen(station.color) / 255f;
                float blue = ColorHelper.Argb.getBlue(station.color) / 255f;
                drawCuboidShapeOutline(matrices, vertexConsumer, state.getOutlineShape(world, pos, ShapeContext.of(entity)), (double)pos.getX() - cameraX, (double)pos.getY() - cameraY, (double)pos.getZ() - cameraZ, red, green, blue, 0.4F);

//                double offsetX = (double)pos.getX() - cameraX;
//                double offsetY = (double)pos.getY() - cameraY;
//                double offsetZ = (double)pos.getZ() - cameraZ;
//                MatrixStack.Entry entry = matrices.peek();
//                VoxelShape shape = VoxelShapes.fullCube();
//                shape.forEachEdge((minX, minY, minZ, maxX, maxY, maxZ) -> {
//                    float k = (float)(maxX - minX);
//                    float l = (float)(maxY - minY);
//                    float m = (float)(maxZ - minZ);
//                    float n = MathHelper.sqrt(k * k + l * l + m * m);
//                    k /= n;
//                    l /= n;
//                    m /= n;
//                    RenderSystem.setShader(GameRenderer::getPositionColorShader);
//                    // .normal(entry.getNormalMatrix(), k, l, m)
//                    vertexConsumer.vertex(entry.getPositionMatrix(), (float)(minX + offsetX), (float)(minY + offsetY), (float)(minZ + offsetZ)).color(red, green, blue, 0.2f).next();
//                    vertexConsumer.vertex(entry.getPositionMatrix(), (float)(maxX + offsetX), (float)(maxY + offsetY), (float)(maxZ + offsetZ)).color(red, green, blue, 0.2f).next();
//                });
            }
        }
    }
}
