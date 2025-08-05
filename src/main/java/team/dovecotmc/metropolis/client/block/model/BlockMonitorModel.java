package team.dovecotmc.metropolis.client.block.model;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class BlockMonitorModel implements UnbakedModel, BakedModel, FabricBakedModel {
    private BakedModel parent = null;

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        ModelBlockRenderer blockRenderer = mc.getBlockRenderer().getModelRenderer();
//        blockRenderer.render(
//                blockView,
//                this.parent,
//                state,
//                pos,
//                RenderSystem.getModelViewStack(),
//
//        );
//        blockRenderer.render();

//        context.pushTransform(quad -> {
//            return false;
//        });
//        TerrainRenderContext
//        System.out.println(RenderSystem.isOnRenderThread());
//        System.out.println(114514);
//        System.out.println(context.bakedModelConsumer().getClass());
//        context.bakedModelConsumer().accept(this.parent);
    }

    @Override
    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.parent.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.parent.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.parent.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.parent.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.parent.getOverrides();
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
//        return List.of(
//                new Identifier(Metropolis.MOD_ID, "block/monitor")
//        );
        return Collections.emptyList();
    }

    @Override
    public Collection<Material> getMaterials(Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return List.of(
                new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("data", "block/light_gray_concrete"))
        );
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBakery loader, Function<Material, TextureAtlasSprite> textureGetter, ModelState rotationContainer, ResourceLocation modelId) {
//        for(int i = 0; i < 2; ++i) {
//            SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
//        }
        BakedModel model = loader.getModel(new ResourceLocation(Metropolis.MOD_ID, "block/monitor")).bake(loader, textureGetter, rotationContainer, modelId);
//        UnbakedModel model1 = loader.getOrLoadModel(new Identifier(Metropolis.MOD_ID, "block/monitor"));
//        System.out.println(1114514);
//        if (model != null) {
//            System.out.println(model.getClass());
//        }
//        System.out.println(model1.getClass());
//
//        // 用Renderer API构建mesh
//        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
//        MeshBuilder builder = renderer.meshBuilder();
//        QuadEmitter emitter = builder.getEmitter();

//        for
        this.parent = model;
        return this;

    }
}
