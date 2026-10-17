package team.dovecotmc.old.metropolis.mtr;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.server.level.ServerPlayer;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformDoor;
import team.dovecotmc.old.metropolis.util.MtrCommonUtil;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

public final class MtrPlatformDoorIntegration {
    private MtrPlatformDoorIntegration() {
    }

    public static void initialize() {
        UseBlockCallback.EVENT.register((player, level, hand, hitResult) -> {
            if (hand != InteractionHand.MAIN_HAND) {
                return InteractionResult.PASS;
            }

            BlockPos clickedPos = hitResult.getBlockPos();
            BlockState clickedState = level.getBlockState(clickedPos);
            if (!(clickedState.getBlock() instanceof AbstractBlockPlatformDoor)
                    || player.getItemInHand(hand).getItem() != MtrCommonUtil.getBrushItem()) {
                return InteractionResult.PASS;
            }

            BlockPos doorPos = clickedState.getValue(AbstractBlockPlatformDoor.HALF) == DoubleBlockHalf.UPPER
                    ? clickedPos.below()
                    : clickedPos;
            if (!(level.getBlockEntity(doorPos) instanceof BlockEntityPlatformDoor doorEntity)) {
                return InteractionResult.PASS;
            }

            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }

            boolean enabled = doorEntity.isAutomaticOpeningEnabled();
            Set<BlockPos> connectedDoors = findConnectedDoors(level, doorPos);
            for (BlockPos connectedPos : connectedDoors) {
                if (level.getBlockEntity(connectedPos) instanceof BlockEntityPlatformDoor connectedEntity) {
                    enabled = connectedEntity.toggleAutomaticOpening();
                    BlockState doorState = level.getBlockState(connectedPos);
                    level.blockEntityChanged(connectedPos);
                    level.sendBlockUpdated(connectedPos, doorState, doorState, 3);
                    BlockPos upperPos = connectedPos.above();
                    BlockState upperState = level.getBlockState(upperPos);
                    level.sendBlockUpdated(upperPos, upperState, upperState, 3);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(connectedEntity.getUpdatePacket());
                    }
                }
            }
            level.playSound(null, doorPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.7F, enabled ? 1.2F : 0.8F);
            player.displayClientMessage(Component.translatable(
                    enabled
                            ? "message.metropolis.platform_door.automatic_opening_enabled"
                            : "message.metropolis.platform_door.automatic_opening_disabled"
            ), true);
            return InteractionResult.SUCCESS;
        });
    }

    private static Set<BlockPos> findConnectedDoors(net.minecraft.world.level.Level level, BlockPos start) {
        Set<BlockPos> connected = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos current = queue.remove();
            if (!connected.add(current)) {
                continue;
            }
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos next = current.relative(direction);
                BlockState nextState = level.getBlockState(next);
                if (nextState.getBlock() instanceof AbstractBlockPlatformDoor
                        && nextState.getValue(AbstractBlockPlatformDoor.HALF) == DoubleBlockHalf.LOWER
                        && level.getBlockEntity(next) instanceof BlockEntityPlatformDoor) {
                    queue.add(next);
                }
            }
        }
        return connected;
    }
}
