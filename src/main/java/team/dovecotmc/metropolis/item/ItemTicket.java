package team.dovecotmc.metropolis.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemTicket extends Item implements InterfaceTicket {
    public static final String BALANCE = "balance";
    public static final String ENTERED_STATION = "entered_station";
    public static final String ENTERED_ZONE = "entered_zone";
    public static final String START_STATION = "start_station";
    public static final String END_STATION = "end_station";
    public final boolean disposable;

    public ItemTicket(Settings settings, boolean disposable) {
        super(settings.maxCount(1));
        this.disposable = disposable;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();

        String stationName = nbt.getString(ENTERED_STATION).split("\\|")[0];
        if (nbt.contains(ENTERED_ZONE))
            tooltip.add(Text.translatable("tooltip.metropolis.ticket.entered_station", stationName));

        if (nbt.contains(START_STATION) && nbt.contains(END_STATION))
            tooltip.add(Text.translatable("tooltip.metropolis.ticket.from_and_to", nbt.getString(START_STATION), nbt.getString(END_STATION)));


        tooltip.add(Text.translatable("tooltip.metropolis.ticket.balance", nbt.getInt(BALANCE)));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
