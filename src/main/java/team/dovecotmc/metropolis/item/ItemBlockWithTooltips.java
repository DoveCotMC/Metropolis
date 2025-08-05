package team.dovecotmc.metropolis.item;

import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MACommonUtil;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;

import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemBlockWithTooltips extends BlockItem {
    public final Style style;
    public ItemBlockWithTooltips(Block block, Properties settings) {
        this(block, settings, Style.EMPTY.withColor(TextColor.fromRgb(DyeColor.GRAY.getTextColor())));
    }

    public ItemBlockWithTooltips(Block block, Properties settings, Style style) {
        super(block, settings);
        this.style = style;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(MACommonUtil.getTooltip(this, this.style));
    }
}
