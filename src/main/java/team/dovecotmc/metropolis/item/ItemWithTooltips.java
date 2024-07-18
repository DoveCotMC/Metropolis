package team.dovecotmc.metropolis.item;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemWithTooltips extends Item {
    public final Style style;
    public ItemWithTooltips(Settings settings) {
        this(settings, Style.EMPTY);
    }

    public ItemWithTooltips(Settings settings, Style style) {
        super(settings);
        this.style = style;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(Text.translatable("tooltip." + Registry.ITEM.getId(this).toTranslationKey()).setStyle(this.style));
    }
}
