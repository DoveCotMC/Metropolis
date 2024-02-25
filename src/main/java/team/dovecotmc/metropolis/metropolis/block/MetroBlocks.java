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
    // TODO: Merge Cables into a single block
//    public static final Block BLOCK_CABLE_A = registerDovecotFeatures("cable_a", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_1 = registerDovecotFeatures("cable_a_1", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_2 = registerDovecotFeatures("cable_a_2", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_3 = registerDovecotFeatures("cable_a_3", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_4 = registerDovecotFeatures("cable_a_4", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_5 = registerDovecotFeatures("cable_a_5", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_6 = registerDovecotFeatures("cable_a_6", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_7 = registerDovecotFeatures("cable_a_7", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_8 = registerDovecotFeatures("cable_a_8", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
//    public static final Block BLOCK_CABLE_A_9 = registerDovecotFeatures("cable_a_9", new BlockCable(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).noCollision().nonOpaque()));
    // Tunnel light
    public static final Block BLOCK_TUNNEL_LIGHT_A = registerDovecotFeatures("tunnel_light_a", new BlockTunnelLight(FabricBlockSettings.of(Material.METAL, MapColor.STONE_GRAY).nonOpaque()));
    public static final Block BLOCK_TICKET_MACHINE_A = registerDovecotFeatures("ticket_machine_a", new BlockSmallerTickerMachine(FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).nonOpaque()));
    public static final Block BLOCK_TICKET_MACHINE_B = registerDovecotFeatures("ticket_machine_b", new BlockTicketMachine(FabricBlockSettings.of(Material.METAL, MapColor.DIAMOND_BLUE).nonOpaque()));
    // Ceiling
    public static final Block BLOCK_TICKET_BARRIER_ENTRANCE_A = registerDovecotFeatures("ticket_barrier_entrance_a", new BlockBeyondTicketBarrier(true));
    public static final Block BLOCK_TICKET_BARRIER_EXIT_A = registerDovecotFeatures("ticket_barrier_exit_a", new BlockBeyondTicketBarrier(false));
    public static final Block BLOCK_CEILING_A = registerDovecotFeatures("ceiling_a", new BlockCeilingA(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).nonOpaque().luminance(1)));
//    public static final Block BLOCK_CEILING_B = registerDovecotFeatures("ceiling_b", new BlockCeilingA(FabricBlockSettings.of(Material.METAL, MapColor.GRAY).nonOpaque().luminance(1)));

    // Train bumpers
    public static final Block BLOCK_BUMPER_A = register("bumper_a", new BumperBlock(FabricBlockSettings.of(Material.METAL, MapColor.YELLOW).nonOpaque()));

    private static Block registerDovecotFeatures(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Metropolis.MOD_ID, id), block);
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, new Identifier(Metropolis.MOD_ID, id), block);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Blocks");
    }
}
