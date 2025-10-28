package team.dovecotmc.metropolis.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

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

    public ItemTicket(Properties settings, boolean disposable) {
        super(settings.stacksTo(1));
        this.disposable = disposable;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        CompoundTag nbt = stack.getOrCreateTag();

        String stationName = nbt.getString(ENTERED_STATION).split("\\|")[0];
        if (nbt.contains(ENTERED_ZONE) && nbt.contains(ENTERED_STATION))
            tooltip.add(MALocalizationUtil.translatableText("tooltip.metropolis.ticket.entered_station", stationName));

        if (nbt.contains(START_STATION) && nbt.contains(END_STATION))
            tooltip.add(MALocalizationUtil.translatableText("tooltip.metropolis.ticket.from_and_to", nbt.getString(START_STATION), nbt.getString(END_STATION)));


        tooltip.add(MALocalizationUtil.translatableText("tooltip.metropolis.ticket.balance", nbt.getInt(BALANCE)));

        super.appendHoverText(stack, world, tooltip, context);
    }
}
