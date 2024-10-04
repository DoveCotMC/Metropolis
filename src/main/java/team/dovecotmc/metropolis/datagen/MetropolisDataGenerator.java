package team.dovecotmc.metropolis.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.Metropolis;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MetropolisDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.addProvider(MetroBlockTagsGenerator::new);
    }

    @Override
    public @Nullable String getEffectiveModId() {
        return Metropolis.MOD_ID;
    }
}
