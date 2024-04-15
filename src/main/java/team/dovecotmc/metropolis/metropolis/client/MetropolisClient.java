package team.dovecotmc.metropolis.metropolis.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.item.ItemStack;
import team.dovecotmc.metropolis.metropolis.Metropolis;
import team.dovecotmc.metropolis.metropolis.block.entity.MetroBlockEntities;
import team.dovecotmc.metropolis.metropolis.client.block.entity.TicketMachineBlockEntityRenderer;
import team.dovecotmc.metropolis.metropolis.client.gui.MetroScreens;
import team.dovecotmc.metropolis.metropolis.client.gui.TicketMachineScreen;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetropolisClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(Metropolis.ID_SCREEN_OPEN_TICKET_MACHINE, (client, handler, buf, responseSender) -> {
            ItemStack stack = buf.readItemStack();
            System.out.println(buf.readVarInt());
            client.execute(() -> {
                client.setScreen(new TicketMachineScreen(stack));
            });
        });

        BlockEntityRendererRegistry.register(MetroBlockEntities.TICKET_MACHINE_BLOCK_ENTITY, ctx -> new TicketMachineBlockEntityRenderer());
    }
}
