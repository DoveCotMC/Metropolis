package team.dovecotmc.metropolis.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import team.dovecotmc.metropolis.block.BlockTrainStopSign;

public class MetroServerNetworking {
    public static final ResourceLocation C2S_TRAIN_STOP_SIGN_CHANGES = new ResourceLocation("metropolis", "c2s_receive_train_stop_sign_changes");
    private static void serverReceiveTrainStopSignChanges() {
        ServerPlayNetworking.registerGlobalReceiver(C2S_TRAIN_STOP_SIGN_CHANGES, (server, player, handler, buf, responseSender) -> {
            int index = buf.readInt();
            BlockPos blockPos = BlockPos.of(buf.readLong());

            server.execute(() -> {
                Level level = player.level();
                BlockState blockState = level.getBlockState(blockPos);
                if (blockState.getBlock() instanceof BlockTrainStopSign) {
                    boolean shouldRemoveItem = !player.isCreative() && blockState.getValue(BlockTrainStopSign.INDEX) != index;
                    ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (itemStack.getItem().equals(Items.INK_SAC)) {
                        level.setBlockAndUpdate(blockPos, level.getBlockState(blockPos).setValue(BlockTrainStopSign.INDEX, index));
                        if (shouldRemoveItem) {
                            itemStack.setCount(itemStack.getCount() - 1);
                            player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
                        }
                    }
                }
            });
        });
    }

    public static void initialize() {
        serverReceiveTrainStopSignChanges();
    }
}
