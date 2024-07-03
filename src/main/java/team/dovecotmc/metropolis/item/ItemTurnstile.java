package team.dovecotmc.metropolis.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.BlockTurnstile;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;

import java.util.List;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemTurnstile extends BlockItem implements IItemShowStationHUD {
    public final BlockEntityTurnstile.EnumTurnstileType type;

    public ItemTurnstile(Block block, Settings settings, BlockEntityTurnstile.EnumTurnstileType type) {
        super(block, settings);
        this.type = type;
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = this.getBlock().getPlacementState(context);
        if (blockState == null || !this.canPlace(context, blockState)) {
            return null;
        }

        return blockState.with(BlockTurnstile.TYPE, type.index);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        // TODO: Translatable
        if (stack.getItem() instanceof ItemTurnstile item) {
            tooltip.add(Text.literal("Type: " + item.type));
        }
    }

    @Override
    public String getTranslationKey() {
        return super.getTranslationKey() + "." + this.type.name().toLowerCase();
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }
}
