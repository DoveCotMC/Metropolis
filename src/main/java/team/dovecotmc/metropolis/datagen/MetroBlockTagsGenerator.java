package team.dovecotmc.metropolis.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import team.dovecotmc.metropolis.Metropolis;

import java.util.Objects;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockTagsGenerator extends FabricTagProvider<Block> {
    private static final TagKey<Block> PICKAXE_KEY = TagKey.create(Registry.BLOCK_REGISTRY, ResourceLocation.tryBuild("data", "mineable/pickaxe"));
    private static final TagKey<Block> NEEDS_STONE_TOOL = TagKey.create(Registry.BLOCK_REGISTRY, ResourceLocation.tryBuild("data", "mineable/needs_stone_tool"));
    private static final TagKey<Block> NEEDS_IRON_TOOL = TagKey.create(Registry.BLOCK_REGISTRY, ResourceLocation.tryBuild("data", "mineable/needs_iron_tool"));

    public MetroBlockTagsGenerator(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.BLOCK);
    }

    @Override
    protected void generateTags() {
        for (ResourceLocation id : this.registry.keySet()) {
            if (!Objects.equals(id.getNamespace(), Metropolis.MOD_ID))
                continue;

            Block block = this.registry.get(id);
            if (block == null)
                continue;

            if (block.defaultBlockState().getMaterial().equals(Material.STONE)) {
                tag(PICKAXE_KEY)
                        .add(block);
//                getOrCreateTagBuilder(NEEDS_STONE_TOOL)
//                        .add(block);
            } else if (block.defaultBlockState().getMaterial().equals(Material.METAL)) {
                tag(PICKAXE_KEY)
                        .add(block);
                tag(NEEDS_STONE_TOOL)
                        .add(block);
            } else {
                Metropolis.LOGGER.info("Unexpected material: " + block.defaultBlockState().getMaterial());
            }
        }
    }
}
