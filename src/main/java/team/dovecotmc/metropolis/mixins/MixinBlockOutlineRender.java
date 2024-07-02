package team.dovecotmc.metropolis.mixins;

import mtr.data.Station;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.dovecotmc.metropolis.block.IBlockStationOverlayShouldRender;
import team.dovecotmc.metropolis.client.MetropolisClient;
import team.dovecotmc.metropolis.util.MtrStationUtil;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@Environment(EnvType.CLIENT)
@Mixin(WorldRenderer.class)
public abstract class MixinBlockOutlineRender {
    @Shadow
    private static void drawCuboidShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {}

    @Shadow @Nullable private ClientWorld world;

    @Inject(at = @At("TAIL"), method = "drawBlockOutline")
    public void renderTail(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (MetropolisClient.BLOCK_PLACE_HUD.shouldRender && world != null) {
            boolean outline = true;
            if (state.getBlock() instanceof IBlockStationOverlayShouldRender config) {
                outline = config.shouldRenderOutline();
            }

            if (outline) {
                Station station = MtrStationUtil.getStationByPos(pos, world);
                if (station != null) {
                    float red = ColorHelper.Argb.getRed(station.color) / 255f;
                    float green = ColorHelper.Argb.getGreen(station.color) / 255f;
                    float blue = ColorHelper.Argb.getBlue(station.color) / 255f;
                    drawCuboidShapeOutline(matrices, vertexConsumer, state.getOutlineShape(world, pos, ShapeContext.of(entity)), (double)pos.getX() - cameraX, (double)pos.getY() - cameraY, (double)pos.getZ() - cameraZ, red, green, blue, 0.4F);
                }
            }
        }
    }
}
