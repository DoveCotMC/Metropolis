package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.BlockDirectionalDoubleBlockBase;
import mtr.block.IBlock;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.entity.BlockEntityTicketMachine;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTicketMachine extends BlockDirectionalDoubleBlockBase implements BlockEntityProvider {
    public BlockTicketMachine() {
        super(Settings.of(Material.METAL).nonOpaque());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.PASS;
        }

//        if (hand == Hand.MAIN_HAND && player.getStackInHand(hand).getItem() instanceof ItemTicket) {
//            System.out.println(player.getStackInHand(hand));
//
//            int emeraldCount = 0;
//            PacketByteBuf buf = PacketByteBufs.create().writeItemStack(player.getStackInHand(hand)).writeVarInt(player.getInventory().count(Items.EMERALD));
//            ServerPlayNetworking.send((ServerPlayerEntity) player, Metropolis.ID_SCREEN_OPEN_TICKET_MACHINE, buf);
//        }

        if (hand != Hand.MAIN_HAND) {
            return ActionResult.PASS;
        }

        if (state.get(IBlock.HALF) == DoubleBlockHalf.LOWER) {
            return ActionResult.PASS;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        BlockEntityTicketMachine entity = (BlockEntityTicketMachine) world.getBlockEntity(pos);

//        // Item Render Modes:
//        // 0: None
//        // 1: Ticket
//        // 2: Card
        NbtCompound nbt = entity.createNbt();
//        System.out.println(nbt);
//
//        // Card
//        if (player.getStackInHand(hand).getItem() instanceof ItemTicket) {
////            ItemTicket item = (ItemTicket) player.getStackInHand(hand).getItem();
////            if (item.disposable) {
////                nbt.putInt(BlockEntityTicketMachine.TAG_ITEM_RENDER_MODE, 2);
////            } else {
////                nbt.putInt(BlockEntityTicketMachine.TAG_ITEM_RENDER_MODE, 1);
////            }
//            nbt.putInt(BlockEntityTicketMachine.TAG_ITEM_RENDER_MODE, 2);
//
//            ItemStack stackBuf = inventory.getStack(0);
//            if (!stackBuf.isEmpty()) {
//                inventory.setStack(0, player.getStackInHand(hand));
//                System.out.println(inventory.getStack(0));
//                player.setStackInHand(hand, inventory.getStack(0));
//            } else {
//                inventory.setStack(0, player.getStackInHand(hand));
//                player.setStackInHand(hand, ItemStack.EMPTY);
//            }
//        } else {
//            nbt.putInt(BlockEntityTicketMachine.TAG_ITEM_RENDER_MODE, 0);
//            System.out.println(inventory.getStack(0));
//            ItemStack stackBuf = inventory.getStack(0);
//            if (!stackBuf.isEmpty()) {
//                player.setStackInHand(hand, inventory.getStack(0));
//                inventory.setStack(0, ItemStack.EMPTY);
//            }
//        }
//

        ItemStack itemStack = player.getStackInHand(hand);
        if (player.getStackInHand(hand).getItem() instanceof ItemTicket) {
            entity.setStack(0, itemStack);
            player.setStackInHand(hand, ItemStack.EMPTY);
            player.getInventory().addPickBlock(entity.getStack(0));
            nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, true);
        } else {
            player.getInventory().addPickBlock(entity.getStack(0));
            entity.removeStack(0);
            nbt.putBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED, false);
        }

        // Sync data
        System.out.println(nbt.getBoolean(BlockEntityTicketMachine.TAG_CARD_SLOT_OCCUPIED));
        System.out.println(nbt);
        entity.readNbt(nbt);
        serverPlayer.networkHandler.sendPacket(entity.toUpdatePacket());
        serverPlayer.networkHandler.tick();

        return ActionResult.SUCCESS;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityTicketMachine(pos, state);
    }

    // Copied from BlockWithEntity
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        super.onSyncedBlockEvent(state, world, pos, type, data);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity == null ? false : blockEntity.onSyncedBlockEvent(type, data);
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NamedScreenHandlerFactory ? (NamedScreenHandlerFactory)blockEntity : null;
    }
}
