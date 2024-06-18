package team.dovecotmc.metropolis.metropolis.item;

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
public class ItemTicket extends Item implements InterfaceTicket {
    public static final String REMAIN_MONEY = "remain_money";
    public static final String ENTERED = "entered";
    public static final String ENTERED_ZONE = "entered_zone";
    public final boolean disposable;

    public ItemTicket(Settings settings, boolean disposable) {
        super(settings.maxCount(1));
        this.disposable = disposable;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();
        tooltip.add(Text.translatable("metropolis.tooltip.ticket.money_remain", nbt.getInt(ItemTicket.REMAIN_MONEY)));

        if (nbt.getBoolean(ItemTicket.ENTERED)) {
            tooltip.add(Text.translatable("metropolis.tooltip.ticket.entered_station_zone", nbt.getInt(ItemTicket.ENTERED_ZONE)));
        } else {
            tooltip.add(Text.translatable("metropolis.tooltip.ticket.no_station_entered"));
        }

        super.appendTooltip(stack, world, tooltip, context);
    }
}
