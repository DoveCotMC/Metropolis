package team.dovecotmc.metropolis.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import mtr.data.Station;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
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
@Mixin(LevelRenderer.class)
public abstract class MixinBlockOutlineRender {
    @Shadow
    private static void renderShape(PoseStack matrices, VertexConsumer vertexConsumer, VoxelShape shape, double offsetX, double offsetY, double offsetZ, float red, float green, float blue, float alpha) {}

    @Shadow @Nullable private ClientLevel level;

    @Shadow protected abstract void renderHitOutline(PoseStack poseStack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState);

    @Inject(at = @At("TAIL"), method = "renderHitOutline")
    public void renderTail(PoseStack matrices, VertexConsumer vertexConsumer, Entity entity, double cameraX, double cameraY, double cameraZ, BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!MetropolisClient.config.enableStationInfoOverlay) {
            return;
        }

        if (MetropolisClient.BLOCK_PLACE_HUD.shouldRender && level != null) {
            boolean outline = true;
            if (state.getBlock() instanceof IBlockStationOverlayShouldRender config) {
                outline = config.shouldRenderOutline();
            }

            if (outline) {
                Station station = MtrStationUtil.getStationByPos(pos, level);
                if (station != null) {
                    float red = FastColor.ARGB32.red(station.color) / 255f;
                    float green = FastColor.ARGB32.green(station.color) / 255f;
                    float blue = FastColor.ARGB32.blue(station.color) / 255f;
                    float alpha = (float) Math.abs((Math.sin((level.getGameTime() + Minecraft.getInstance().getFrameTime()) / 4f) / 2f));
                    renderShape(matrices, vertexConsumer, state.getShape(level, pos, CollisionContext.of(entity)), (double)pos.getX() - cameraX, (double)pos.getY() - cameraY, (double)pos.getZ() - cameraZ, red, green, blue, alpha);
                }
            }
        }
    }
}
