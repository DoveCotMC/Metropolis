package team.dovecotmc.metropolis.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MACommonUtil;

import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemWithTooltips extends Item {
    public final Style toolTipStyle;
    public ItemWithTooltips(Properties settings) {
        this(settings, Style.EMPTY);
    }

    public ItemWithTooltips(Properties settings, Style toolTipStyle) {
        super(settings);
        this.toolTipStyle = toolTipStyle;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(MACommonUtil.getTooltip(this, this.toolTipStyle));
    }
}
