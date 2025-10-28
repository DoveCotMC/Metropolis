package team.dovecotmc.metropolis.item;

import mtr.Blocks;
import mtr.data.RailAngle;
import mtr.data.RailwayData;
import mtr.data.TransportMode;
import mtr.item.ItemNodeModifierBase;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import team.dovecotmc.metropolis.abstractinterface.util.MALocalizationUtil;
import team.dovecotmc.metropolis.client.gui.BridgeCreatorConfigurationScreen;

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
    public InteractionResult useOn(UseOnContext context) {
        // TODO: GUI
        if (!context.getLevel().isClientSide()) {
            BlockState state = context.getLevel().getBlockState(context.getClickedPos());
            CompoundTag nbt = context.getItemInHand().getOrCreateTag();
            Player player = context.getPlayer();

            if (player == null)
                return InteractionResult.SUCCESS;

            if (state.getBlock() != Blocks.RAIL_NODE.get()) {
                if (Objects.requireNonNull(player).isShiftKeyDown()) {
//                    player.sendMessage(MALocalizationUtil.literalText("Width: " + (nbt.getInt(WIDTH) - 2)));
//                    nbt.putInt(WIDTH, nbt.getInt(WIDTH) - 2);
                    player.sendSystemMessage(MALocalizationUtil.literalText("Block: " + MALocalizationUtil.translatableText(state.getBlock().getDescriptionId()).getString()));
                    nbt.putInt(BLOCK_ID, Block.getId(state));
                } else {
                    player.sendSystemMessage(MALocalizationUtil.literalText("Width: " + (nbt.getInt(WIDTH) + 2)));
                    nbt.putInt(WIDTH, nbt.getInt(WIDTH) + 2);
                }
            } else {
                if (nbt.contains(POS_START, CompoundTag.TAG_LONG)) {
                    RailwayData railwayData = RailwayData.getInstance(context.getLevel());
                    BlockPos posStart = BlockPos.of(nbt.getLong(POS_START));
                    BlockPos posEnd = context.getClickedPos();

                    BlockState state1 = nbt.contains(BLOCK_ID) ? Block.stateById(nbt.getInt(BLOCK_ID)) : null;

                    if (!railwayData.containsRail(posStart, posEnd)) {
                        player.displayClientMessage(MALocalizationUtil.translatableText("gui.mtr.rail_not_found_action"), true);
                    } else if (state1 == null) {
                        player.displayClientMessage(MALocalizationUtil.literalText("No block selected"), true);
                    } else {
                        nbt.remove(POS_START);
                        railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, (nbt.getInt(WIDTH) + 1) / 2, state1);
                    }
                } else {
                    player.sendSystemMessage(MALocalizationUtil.literalText("First pos: " + context.getClickedPos().toShortString()));
                    nbt.putLong(POS_START, context.getClickedPos().asLong());
                }
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (!level.isClientSide())
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));

        if (!(Minecraft.getInstance().hitResult instanceof BlockHitResult))
            return InteractionResultHolder.pass(player.getItemInHand(interactionHand));

        CompoundTag nbt = player.getItemInHand(interactionHand).getOrCreateTag();
        Minecraft.getInstance().setScreen(new BridgeCreatorConfigurationScreen(nbt.getInt(WIDTH)));

        return InteractionResultHolder.success(player.getItemInHand(interactionHand));
    }

    @Override
    protected final void onConnect(Level world, ItemStack stack, TransportMode transportMode, BlockState stateStart, BlockState stateEnd, BlockPos posStart, BlockPos posEnd, RailAngle facingStart, RailAngle facingEnd, Player player, RailwayData railwayData) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (player != null && !this.onConnect(player, stack, railwayData, posStart, posEnd, nbt.getInt(WIDTH), nbt.getInt(HEIGHT))) {
            player.displayClientMessage(MALocalizationUtil.translatableText("gui.mtr.rail_not_found_action"), true);
        }
    }

    @Override
    protected void onRemove(Level world, BlockPos blockPos, BlockPos blockPos1, Player playerEntity, RailwayData railwayData) {
    }

    protected boolean onConnect(Player player, ItemStack stack, RailwayData railwayData, BlockPos posStart, BlockPos posEnd, int radius, int height) {
        CompoundTag nbt = stack.getOrCreateTag();
        BlockState state = Block.stateById(nbt.getInt(BLOCK_ID));
//        railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, this.w, state);
//        return state == null || railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, radius, state);
        return state == null || railwayData.railwayDataRailActionsModule.markRailForBridge(player, posStart, posEnd, nbt.getInt(WIDTH) / 2, state);
    }

    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        CompoundTag nbt = stack.getOrCreateTag();

        tooltip.add(MALocalizationUtil.translatableText("tooltip.mtr.rail_action_width", nbt.getInt(WIDTH) + 1).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        if (nbt.getInt(HEIGHT) > 0) {
            tooltip.add(MALocalizationUtil.translatableText("tooltip.mtr.rail_action_height", nbt.getInt(HEIGHT)).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY)));
        }

        BlockState state = Block.stateById(nbt.getInt(BLOCK_ID));
        String[] textSplit = MALocalizationUtil.translatableText(state.isAir() ? "tooltip.mtr.shift_right_click_to_select_material" : "tooltip.mtr.shift_right_click_to_clear", Minecraft.getInstance().options.keyShift.getTranslatedKeyMessage(), MALocalizationUtil.translatableText(((Block)mtr.Blocks.RAIL_NODE.get()).getDescriptionId())).getString().split("\\|");

        for (String text : textSplit) {
            tooltip.add(MALocalizationUtil.literalText(text).setStyle(Style.EMPTY.withColor(ChatFormatting.GRAY).applyFormat(ChatFormatting.ITALIC)));
        }

        tooltip.add(MALocalizationUtil.translatableText("tooltip.mtr.selected_material", MALocalizationUtil.translatableText(state.getBlock().getDescriptionId())).setStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)));
    }
}
