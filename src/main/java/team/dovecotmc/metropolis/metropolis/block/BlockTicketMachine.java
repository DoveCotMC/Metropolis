package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.BlockDirectionalDoubleBlockBase;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import team.dovecotmc.metropolis.metropolis.client.gui.MetroScreens;
import team.dovecotmc.metropolis.metropolis.item.ItemTicket;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockTicketMachine extends BlockDirectionalDoubleBlockBase {
    public BlockTicketMachine() {
        super(Settings.of(Material.METAL).nonOpaque());
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.PASS;
        }
//        CraftingScreenHandler

        if (hand == Hand.MAIN_HAND && player.getStackInHand(hand).getItem() instanceof ItemTicket) {
            System.out.println(player.getStackInHand(hand));
            ServerPlayNetworking.send((ServerPlayerEntity) player, MetroScreens.ID_SCREEN_OPEN_TICKET_MACHINE, PacketByteBufs.create().writeItemStack(player.getStackInHand(hand)));
        }

        return ActionResult.SUCCESS;
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}
