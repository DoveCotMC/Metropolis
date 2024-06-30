package team.dovecotmc.metropolis.item;

import mtr.Blocks;
import mtr.MTR;
import mtr.MTRClient;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.item.ItemBlockClickingBase;
import mtr.item.ItemNodeModifierBase;
import mtr.item.ItemNodeModifierSelectableBlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class ItemDynamicBridgeCreator extends ItemNodeModifierBase {
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String BLOCK_ID = "block_id";
    public static final String POS_START = "pos_start";

    public ItemDynamicBridgeCreator() {
        super(true, false, false, true);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        // TODO: GUI
        if (!context.getWorld().isClient()) {
            BlockState state = context.getWorld().getBlockState(context.getBlockPos());
            NbtCompound nbt = context.getStack().getOrCreateNbt();
            if (state.getBlock() != Blocks.RAIL_NODE.get()) {
                if (Objects.requireNonNull(context.getPlayer()).isSneaking()) {
//                    context.getPlayer().sendMessage(Text.literal("Width: " + (nbt.getInt(WIDTH) - 2)));
//                    nbt.putInt(WIDTH, nbt.getInt(WIDTH) - 2);
                    context.getPlayer().sendMessage(Text.literal("Block: " + mtr.mappings.Text.translatable(state.getBlock().getTranslationKey()).getString()));
                    nbt.putInt(BLOCK_ID, Block.getRawIdFromState(state));
                } else {
                    context.getPlayer().sendMessage(Text.literal("Width: " + (nbt.getInt(WIDTH) + 2)));
                    nbt.putInt(WIDTH, nbt.getInt(WIDTH) + 2);
                }
            } else {
                if (nbt.contains(POS_START, NbtCompound.LONG_TYPE)) {
                    RailwayData railwayData = RailwayData.getInstance(context.getWorld());
                    BlockPos posStart = BlockPos.fromLong(nbt.getLong(POS_START));
                    BlockPos posEnd = context.getBlockPos();

                    BlockState state1 = nbt.contains(BLOCK_ID) ? Block.getStateFromRawId(nbt.getInt(BLOCK_ID)) : null;

                    if (!railwayData.containsRail(posStart, posEnd)) {
                        context.getPlayer().sendMessage(mtr.mappings.Text.translatable("gui.mtr.rail_not_found_action"), true);
                    } else if (state1 == null) {
                        context.getPlayer().sendMessage(mtr.mappings.Text.literal("No block selected"), true);
                    } else {
                        nbt.remove(POS_START);
                        railwayData.railwayDataRailActionsModule.markRailForBridge(context.getPlayer(), posStart, posEnd, (nbt.getInt(WIDTH) + 1) / 2, state1);
                    }
                } else {
                    context.getPlayer().sendMessage(Text.literal("First pos: " + context.getBlockPos().toShortString()));
                    nbt.putLong(POS_START, context.getBlockPos().asLong());
                }
            }
        }

        return ActionResult.SUCCESS;
    }

    @Override
    protected final void onConnect(World world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, PlayerEntity player, RailwayData railwayData) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (player != null && !this.onConnect(player, stack, railwayData, posStart, posEnd, nbt.getInt(WIDTH), nbt.getInt(HEIGHT))) {
            player.sendMessage(mtr.mappings.Text.translatable("gui.mtr.rail_not_found_action"), true);
        }
    }

    @Override
    protected void onRemove(World world, BlockPos blockPos, BlockPos blockPos1, PlayerEntity playerEntity, RailwayData railwayData) {
    }

    protected boolean onConnect(PlayerEntity player, ItemStack stack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height) {
        NbtCompound nbt = stack.getOrCreateNbt();
        BlockState state = Block.getStateFromRawId(nbt.getInt(BLOCK_ID));
//        railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, this.w, state);
//        return state == null || railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, radius, state);
        return state == null || railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, nbt.getInt(WIDTH) / 2, state);
    }

    public void appendTooltip(ItemStack stack, World level, List<Text> tooltip, TooltipContext tooltipFlag) {
        NbtCompound nbt = stack.getOrCreateNbt();

        tooltip.add(mtr.mappings.Text.translatable("tooltip.mtr.rail_action_width", nbt.getInt(WIDTH) + 1).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        if (nbt.getInt(HEIGHT) > 0) {
            tooltip.add(mtr.mappings.Text.translatable("tooltip.mtr.rail_action_height", nbt.getInt(HEIGHT)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }

        BlockState state = Block.getStateFromRawId(nbt.getInt(BLOCK_ID));
        String[] textSplit = mtr.mappings.Text.translatable(state.isAir() ? "tooltip.mtr.shift_right_click_to_select_material" : "tooltip.mtr.shift_right_click_to_clear", MinecraftClient.getInstance().options.sneakKey.getBoundKeyLocalizedText(), mtr.mappings.Text.translatable(((Block)mtr.Blocks.RAIL_NODE.get()).getTranslationKey())).getString().split("\\|");

        for (String text : textSplit) {
            tooltip.add(mtr.mappings.Text.literal(text).setStyle(Style.EMPTY.withColor(Formatting.GRAY).withFormatting(Formatting.ITALIC)));
        }

        tooltip.add(mtr.mappings.Text.translatable("tooltip.mtr.selected_material", mtr.mappings.Text.translatable(state.getBlock().getTranslationKey())).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
