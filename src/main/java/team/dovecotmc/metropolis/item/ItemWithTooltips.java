package team.dovecotmc.metropolis.item;

import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MACommonUtil;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemWithTooltips extends Item {
    public final Style style;
    public ItemWithTooltips(Properties settings) {
        this(settings, Style.EMPTY);
    }

    public ItemWithTooltips(Properties settings, Style style) {
        super(settings);
        this.style = style;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(MACommonUtil.getTooltip(this, this.style));
    }
}
