package team.dovecotmc.metropolis.sittable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public record SittableRegistry(
        Block block,
        OffsetProvider offset) {
    @FunctionalInterface
    public interface OffsetProvider {
        @SuppressWarnings("all")
        Optional<Vec3d> get(BlockState state, PlayerEntity player, Optional<BlockHitResult> hit);
    }

    public SittableRegistry(Block block) {
        this(block, (s, p, h) -> Optional.of(new Vec3d(.5, .5, .5)));
    }
}