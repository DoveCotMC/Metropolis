package team.dovecotmc.metropolis.sittable;

import java.util.Optional;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public record SittableRegistry(
        Block block,
        OffsetProvider offset) {
    @FunctionalInterface
    public interface OffsetProvider {
        @SuppressWarnings("all")
        Optional<Vec3> get(BlockState state, Player player, Optional<BlockHitResult> hit);
    }

    public SittableRegistry(Block block) {
        this(block, (s, p, h) -> Optional.of(new Vec3(.5, .5, .5)));
    }
}