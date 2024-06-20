package team.dovecotmc.metropolis.metropolis;

import mtr.client.CustomResources;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.dovecotmc.metropolis.metropolis.block.MetroBlocks;
import team.dovecotmc.metropolis.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.metropolis.item.MetroItems;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class Metropolis implements ModInitializer {
    public static final String MOD_ID = "metropolis";
    public static final Logger LOGGER = LogManager.getLogger("Metropolis");
    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "decorations"))
            .icon(() -> new ItemStack(Blocks.TNT))
            .build();

    public static final Identifier ID_SCREEN_OPEN_TICKET_MACHINE = new Identifier(Metropolis.MOD_ID, "open_ticket_machine");
    public static final Identifier ID_SCREEN_CLOSE_TICKET_MACHINE = new Identifier(Metropolis.MOD_ID, "close_ticket_machine");

    @Override
    public void onInitialize() {
        MetroBlocks.initialize();
        MetroBlockEntities.initialize();
        MetroItems.initialize();

        ServerPlayNetworking.registerGlobalReceiver(ID_SCREEN_CLOSE_TICKET_MACHINE, (server, player, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
            server.execute(() -> {
                player.setStackInHand(Hand.MAIN_HAND, stack);
            });
        });
    }
}
