package team.dovecotmc.metropolis.item;

import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.block.BlockTurnstile;
import team.dovecotmc.metropolis.block.entity.BlockEntityTurnstile;

import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemTurnstile extends BlockItem implements IItemShowStationHUD {
    public final BlockEntityTurnstile.EnumTurnstileType type;

    public ItemTurnstile(Block block, Properties settings, BlockEntityTurnstile.EnumTurnstileType type) {
        super(block, settings);
        this.type = type;
    }

    @Nullable
    @Override
    protected BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockState = this.getBlock().getStateForPlacement(context);
        if (blockState == null || !this.canPlace(context, blockState)) {
            return null;
        }

        return blockState.setValue(BlockTurnstile.TYPE, type.index);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag context) {
        // TODO: Translatable
//        if (stack.getItem() instanceof ItemTurnstile item) {
//            tooltip.add(MALocalizationUtil.literalText("Type: " + item.type));
//        }
        super.appendHoverText(stack, world, tooltip, context);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId() + "." + this.type.name().toLowerCase();
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> stacks) {
        if (this.allowedIn(group)) {
            stacks.add(new ItemStack(this));
        }
    }
}
