package team.dovecotmc.metropolis.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.abstractinterface.util.MACommonUtil;

import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemBlockWithTooltipShowStationHUD extends BlockItem implements IItemShowStationHUD {
    public final Style style;
    public ItemBlockWithTooltipShowStationHUD(Block block, Item.Properties settings) {
        this(block, settings, Style.EMPTY);
    }

    public ItemBlockWithTooltipShowStationHUD(Block block, Item.Properties settings, Style style) {
        super(block, settings);
        this.style = style;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        super.appendHoverText(stack, world, tooltip, context);
        tooltip.add(MACommonUtil.getTooltip(this, this.style));
    }
}
