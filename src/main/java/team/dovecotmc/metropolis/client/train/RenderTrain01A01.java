package team.dovecotmc.metropolis.client.train;

import fabric.cn.zbx1425.mtrsteamloco.MainClient;
import fabric.cn.zbx1425.sowcerext.model.RawModel;
import fabric.cn.zbx1425.sowcerext.model.loader.ObjModelLoader;
import mtr.data.TrainClient;
import mtr.render.TrainRendererBase;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import team.dovecotmc.metropolis.Metropolis;

import java.io.IOException;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class RenderTrain01A01 extends TrainRendererBase {
    private static final RawModel[] models = new RawModel[1];
    public final TrainClient train;

    public RenderTrain01A01(TrainClient train) {
        this.train = train;
    }

    public static void initGLModel(ResourceManager resourceManager) {
        try {
            models[0] = ObjModelLoader.loadModel(resourceManager, new Identifier(Metropolis.MOD_ID, "trains/01a01/01a01.obj"), MainClient.atlasManager);
        } catch (IOException exception) {
            Metropolis.LOGGER.error("Failed loading model for 01A01: ", exception);
        }
    }

    @Override
    public TrainRendererBase createTrainInstance(TrainClient trainClient) {
        return new RenderTrain01A01(trainClient);
    }

    @Override
    public void renderCar(int i, double v, double v1, double v2, float v3, float v4, boolean b, boolean b1) {
    }

    @Override
    public void renderConnection(Vec3d vec3d, Vec3d vec3d1, Vec3d vec3d2, Vec3d vec3d3, Vec3d vec3d4, Vec3d vec3d5, Vec3d vec3d6, Vec3d vec3d7, double v, double v1, double v2, float v3, float v4) {
    }

    @Override
    public void renderBarrier(Vec3d vec3d, Vec3d vec3d1, Vec3d vec3d2, Vec3d vec3d3, Vec3d vec3d4, Vec3d vec3d5, Vec3d vec3d6, Vec3d vec3d7, double v, double v1, double v2, float v3, float v4) {
    }
}
