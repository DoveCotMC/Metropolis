package team.dovecotmc.metropolis.item;

import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
    public final boolean infiniteBalance;

    public ItemCard(Properties settings, boolean infiniteBalance) {
        super(settings.stacksTo(1));
        this.infiniteBalance = infiniteBalance;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return infiniteBalance;
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        if (infiniteBalance) {
            tooltip.add(MALocalizationUtil.translatableText("tooltip.metropolis.infinity_balance"));
        } else {
            CompoundTag nbt = stack.getOrCreateTag();

            String stationName = nbt.getString(ENTERED_STATION).split("\\|")[0];
            if (nbt.contains(ENTERED_ZONE) && nbt.contains(ENTERED_STATION))
                tooltip.add(MALocalizationUtil.translatableText("tooltip.metropolis.ticket.entered_station", stationName));

            String value = MALocalizationUtil.translatableText("misc.metropolis.cost", nbt.getInt(BALANCE)).getString();
            String maxValue = MALocalizationUtil.translatableText("misc.metropolis.cost", nbt.getInt(MAX_VALUE)).getString();
            tooltip.add(MALocalizationUtil.translatableText("tooltip.metropolis.card.balance", value, maxValue));
        }

        super.appendHoverText(stack, world, tooltip, context);
    }
}
