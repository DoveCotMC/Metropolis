package team.dovecotmc.metropolis.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import team.dovecotmc.metropolis.client.gui.GUIManager;

public class ItemSketchBoard extends ItemWithTooltips {
    public ItemSketchBoard(Properties settings) {
        super(settings, Style.EMPTY.withColor(TextColor.fromRgb(DyeColor.GRAY.getTextColor())));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (level.isClientSide()) {
            GUIManager.openPixelArtScreen();
        }

        return InteractionResultHolder.success(itemStack);
    }
}
