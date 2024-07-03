package team.dovecotmc.metropolis.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemCard extends Item implements InterfaceTicket {
    public static final String BALANCE = "balance";
    public static final String MAX_VALUE = "max_value";
    public static final String ENTERED_STATION = "entered_station";
    public static final String ENTERED_ZONE = "entered_zone";

    public ItemCard(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();

        String stationName = nbt.getString(ENTERED_STATION).split("\\|")[0];
        if (nbt.contains(ENTERED_ZONE) && nbt.contains(ENTERED_STATION))
            tooltip.add(Text.translatable("tooltip.metropolis.ticket.entered_station", stationName));

        String value = Text.translatable("misc.metropolis.cost", nbt.getInt(BALANCE)).getString();
        String maxValue = Text.translatable("misc.metropolis.cost", nbt.getInt(MAX_VALUE)).getString();
        tooltip.add(Text.translatable("tooltip.metropolis.card.balance", value, maxValue));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
