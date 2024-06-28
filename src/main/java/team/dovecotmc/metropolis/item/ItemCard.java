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

    public ItemCard(Settings settings) {
        super(settings.maxCount(1));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateNbt();

        tooltip.add(Text.translatable("tooltip.metropolis.card.balance", nbt.getInt(BALANCE)));

        super.appendTooltip(stack, world, tooltip, context);
    }
}
