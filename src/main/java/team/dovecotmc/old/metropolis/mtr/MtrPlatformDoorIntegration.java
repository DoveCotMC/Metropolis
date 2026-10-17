package team.dovecotmc.old.metropolis.mtr;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;
import team.dovecotmc.metropolis.block.platform_doors.AbstractBlockPlatformDoor;
import team.dovecotmc.old.metropolis.util.MtrCommonUtil;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

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

            boolean enabled = doorEntity.toggleAutomaticOpening();
            BlockState doorState = level.getBlockState(doorPos);
            level.blockEntityChanged(doorPos);
            level.sendBlockUpdated(doorPos, doorState, doorState, 3);
            level.playSound(null, doorPos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.7F, enabled ? 1.2F : 0.8F);
            player.displayClientMessage(Component.translatable(
                    enabled
                            ? "message.metropolis.platform_door.automatic_opening_enabled"
                            : "message.metropolis.platform_door.automatic_opening_disabled"
            ), true);
            return InteractionResult.SUCCESS;
        });
    }
}
