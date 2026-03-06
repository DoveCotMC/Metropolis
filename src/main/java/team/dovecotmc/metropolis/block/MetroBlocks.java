package team.dovecotmc.metropolis.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import team.dovecotmc.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2023 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public class MetroBlocks {
    // Cables
    public static final Block BLOCK_CABLE = register("cable", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
    public static final Block BLOCK_CABLE_HORIZONTAL = register("cable_horizontal", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
    public static final Block BLOCK_CABLE_INNER_CORNER_LEFT = register("cable_inner_corner_left", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), true, false, false));
    public static final Block BLOCK_CABLE_INNER_CORNER_RIGHT = register("cable_inner_corner_right", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), true, false, true));
    public static final Block BLOCK_CABLE_OUTER_CORNER_LEFT = register("cable_outer_corner_left", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, true, false));
    public static final Block BLOCK_CABLE_OUTER_CORNER_RIGHT = register("cable_outer_corner_right", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, true, true));
    public static final Block BLOCK_CABLE_DOWN_LEFT = register("cable_down_left", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
    public static final Block BLOCK_CABLE_DOWN_RIGHT = register("cable_down_right", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
    public static final Block BLOCK_CABLE_UP_LEFT = register("cable_up_left", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
    public static final Block BLOCK_CABLE_UP_RIGHT = register("cable_up_right", new BlockCable(FabricBlockSettings.create().mapColor(MapColor.STONE).strength(2.0f).noCollission().noOcclusion(), false, false, false));
    // Tunnel light
//    public static final Block BLOCK_TUNNEL_LIGHT_A = register("tunnel_light_a", new BlockTunnelLight(FabricBlockSettings.create().mapColor(MapColor.STONE_GRAY).nonOpaque()));

    // Ceiling
    public static final Block BLOCK_CEILING_A = register("ceiling_a", new BlockCeilingA(FabricBlockSettings.create().mapColor(MapColor.COLOR_GRAY).strength(6.0f).noOcclusion().lightLevel((blockState) -> 1)));

    // Lamp
    public static final Block BLOCK_FLUORESCENT_LAMP = register("fluorescent_lamp", new BlockFluorescentLamp());
    // Train bumpers
    public static final Block BLOCK_BUMPER = register("bumper", new BlockBumper());
    // Cameras
    public static final Block BLOCK_CAMERA_CEILING = register("camera_ceiling", new BlockCameraCeiling());

    // Sign
    public static final Block BLOCK_SIGN_NO_PHOTO = register("sign_no_photo", new BlockSign(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(6.0f)));

    // PIDS
//    public static final Block BLOCK_PIDS_1 = register("pids_1", new BlockMetroPIDSOne());

    // Monitor
    public static final Block BLOCK_ITV_MONITOR = register("itv_monitor", new BlockITVMonitor());

    /* =========== *
     * Decorations *
     * =========== */
    // Concrete
    public static final Block BLOCK_CONCRETE = register("concrete", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));

    // Tiles
    // White
    public static final Block BLOCK_TILES_WHITE = register("tiles_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_TILES_LARGE_WHITE = register("tiles_large_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_TILES_HORIZONTAL_WHITE = register("tiles_horizontal_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_TILES_SMALL_WHITE = register("tiles_small_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE.getMapColor()).strength(6.0f)));
    // Gray
    public static final Block BLOCK_TILES_GRAY = register("tiles_gray", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_TILES_HORIZONTAL_GRAY = register("tiles_horizontal_gray", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_TILES_SMALL_GRAY = register("tiles_small_gray", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));
    // Cordon line
    public static final Block BLOCK_CORDON_YELLOW_BLACK = register("cordon_yellow_black", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_CORDON_YELLOW_WHITE = register("cordon_yellow_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_CORDON_RED_BLACK = register("cordon_red_black", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.RED.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_CORDON_RED_WHITE = register("cordon_red_white", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.RED.getMapColor()).strength(6.0f)));

    // Bench
    public static final Block BLOCK_BENCH = register("bench", new BlockBench(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));

    // Awning
    public static final Block BLOCK_AWNING_PILLAR = register("awning_pillar", new BlockAwningPillar(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_AWNING_PILLAR_EMERGENCY = register("awning_pillar_emergency", new BlockAwningPillarEmergency(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_AWNING_BEAM = register("awning_beam", new BlockAwningBeam(BlockBehaviour.Properties.of().mapColor(DyeColor.LIGHT_GRAY.getMapColor()).strength(6.0f)));
    public static final Block BLOCK_AWNING_ROOF = register("awning_roof", new BlockAwningRoof(BlockBehaviour.Properties.of().mapColor(DyeColor.GRAY.getMapColor()).strength(6.0f)));

    // Blind path
    public static final Block BLOCK_BLIND_PATH = register("blind_path", new BlockBlindPath(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor()).strength(4.0f)));

    // TODO: Blind path
//    public static final Block BLOCK_BLIND_PATH_STRIP = register("blind_path_strip", new BlockHorizontalAxis(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor())));
//    public static final Block BLOCK_BLIND_PATH_POINT = register("blind_path_point", new Block(BlockBehaviour.Properties.of().mapColor(DyeColor.YELLOW.getMapColor())));

    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Metropolis.MOD_ID, id), block);
    }

    public static void initialize() {
        Metropolis.LOGGER.info("Initializing Blocks");
    }
}