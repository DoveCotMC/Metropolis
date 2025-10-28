package team.dovecotmc.metropolis.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import team.dovecotmc.metropolis.Metropolis;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetroBlockTagsGenerator extends FabricTagProvider<Block> {
    private static final TagKey<Block> PICKAXE_KEY = TagKey.create(Registries.BLOCK, new ResourceLocation("mineable/pickaxe"));
    private static final TagKey<Block> NEEDS_STONE_TOOL = TagKey.create(Registries.BLOCK, new ResourceLocation("needs_stone_tool"));
    private static final TagKey<Block> NEEDS_IRON_TOOL = TagKey.create(Registries.BLOCK, new ResourceLocation("needs_iron_tool"));


    public MetroBlockTagsGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);

            if (!Objects.equals(id.getNamespace(), Metropolis.MOD_ID))
                continue;

            if (block.defaultBlockState().is(net.minecraft.tags.BlockTags.STONE_ORE_REPLACEABLES)) {
                getOrCreateTagBuilder(PICKAXE_KEY)
                        .add(block);
            } else if (block.defaultBlockState().is(net.minecraft.tags.BlockTags.IRON_ORES)) {
                getOrCreateTagBuilder(PICKAXE_KEY)
                        .add(block);
                getOrCreateTagBuilder(NEEDS_STONE_TOOL)
                        .add(block);
            } else {
                Metropolis.LOGGER.info("Unexpected block type: " + id);
            }
        }
    }
}