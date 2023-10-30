package team.dovecotmc.metropolis.metropolis.block;

import mtr.block.BlockTicketMachine;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
public class MetroBlocks {
    public static final Block BLOCK_CABLE_A = register("cable_a", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_1 = register("cable_a_1", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_2 = register("cable_a_2", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_3 = register("cable_a_3", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_4 = register("cable_a_4", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_5 = register("cable_a_5", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_6 = register("cable_a_6", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_7 = register("cable_a_7", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_8 = register("cable_a_8", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    public static final Block BLOCK_CABLE_A_9 = register("cable_a_9", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    // 隧道灯
    public static final Block BLOCK_TUNNEL_LIGHT_A = register("tunnel_light_a", new BlockTunnelLight(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).nonOpaque()));
    public static final Block BLOCK_TICKET_MACHINE_0 = register("ticket_machine_0", new BlockSmallerTickerMachine(FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).nonOpaque()));
    public static final Block BLOCK_TICKET_MACHINE_1 = register("ticket_machine_1", new BlockTicketMachine(FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).nonOpaque()));
    // 天花板
    public static final Block BLOCK_TICKET_BARRIER_ENTRANCE_0 = register("ticket_barrier_entrance_0", new BlockBeyondTicketBarrier(true));
    public static final Block BLOCK_TICKET_BARRIER_EXIT_0 = register("ticket_barrier_exit_0", new BlockBeyondTicketBarrier(false));
    public static final Block BLOCK_CEILING_A = register("ceiling_a", new BlockCeilingA(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).nonOpaque().luminance(1)));
    public static final Block BLOCK_CEILING_A_LIGHT = register("ceiling_a_light", new BlockCeilingA(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).nonOpaque().luminance(15)));

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Metropolis.MOD_ID, id), block);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Blocks");
    }
}
