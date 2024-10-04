package team.dovecotmc.metropolis.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import team.dovecotmc.metropolis.Metropolis;

import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockTagsGenerator extends FabricTagProvider<Block> {
    private static final TagKey<Block> PICKAXE_KEY = TagKey.of(Registry.BLOCK_KEY, Identifier.of("data", "mineable/pickaxe"));
    private static final TagKey<Block> NEEDS_STONE_TOOL = TagKey.of(Registry.BLOCK_KEY, Identifier.of("data", "mineable/needs_stone_tool"));
    private static final TagKey<Block> NEEDS_IRON_TOOL = TagKey.of(Registry.BLOCK_KEY, Identifier.of("data", "mineable/needs_iron_tool"));

    public MetroBlockTagsGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.BLOCK);
    }

    @Override
    protected void generateTags() {
        for (Identifier id : this.registry.getIds()) {
            if (!Objects.equals(id.getNamespace(), Metropolis.MOD_ID))
                continue;

            Block block = this.registry.get(id);
            if (block == null)
                continue;

            if (block.getDefaultState().getMaterial().equals(Material.STONE)) {
                getOrCreateTagBuilder(PICKAXE_KEY)
                        .add(block);
//                getOrCreateTagBuilder(NEEDS_STONE_TOOL)
//                        .add(block);
            } else if (block.getDefaultState().getMaterial().equals(Material.METAL)) {
                getOrCreateTagBuilder(PICKAXE_KEY)
                        .add(block);
                getOrCreateTagBuilder(NEEDS_STONE_TOOL)
                        .add(block);
            } else {
                Metropolis.LOGGER.info("Unexpected material: " + block.getDefaultState().getMaterial());
            }
        }
    }
}
