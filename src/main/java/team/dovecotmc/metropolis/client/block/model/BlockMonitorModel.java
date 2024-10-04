package team.dovecotmc.metropolis.client.block.model;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.model.*;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
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
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        MinecraftClient mc = MinecraftClient.getInstance();
        BlockModelRenderer blockRenderer = mc.getBlockRenderManager().getModelRenderer();
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
    public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
        return Collections.emptyList();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.parent.useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth() {
        return this.parent.hasDepth();
    }

    @Override
    public boolean isSideLit() {
        return false;
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Sprite getParticleSprite() {
        return this.parent.getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return this.parent.getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides() {
        return this.parent.getOverrides();
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
//        return List.of(
//                new Identifier(Metropolis.MOD_ID, "block/monitor")
//        );
        return Collections.emptyList();
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
        return List.of(
                new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("data", "block/light_gray_concrete"))
        );
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
//        for(int i = 0; i < 2; ++i) {
//            SPRITES[i] = textureGetter.apply(SPRITE_IDS[i]);
//        }
        BakedModel model = loader.getOrLoadModel(new Identifier(Metropolis.MOD_ID, "block/monitor")).bake(loader, textureGetter, rotationContainer, modelId);
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
