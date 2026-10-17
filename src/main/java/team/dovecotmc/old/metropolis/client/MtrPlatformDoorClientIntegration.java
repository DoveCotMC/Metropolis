package team.dovecotmc.old.metropolis.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.render.PositionAndRotation;
import org.mtr.mod.render.RenderVehicleHelper;
import team.dovecotmc.metropolis.block.entity.BlockEntityPlatformDoor;

import java.util.HashSet;
import java.util.Set;

/** Queries existing vehicles when doors load or receive a mode update, independently of rendering. */
public final class MtrPlatformDoorClientIntegration {
    private static final Set<BlockEntityPlatformDoor> pending = new HashSet<>();
    private static ClientLevel lastWorld;
    private static long nextRefreshTick;

    private MtrPlatformDoorClientIntegration() {
    }

    public static void initialize() {
        BlockEntityPlatformDoor.setClientStateInitializer(pending::add);
        ClientTickEvents.END_CLIENT_TICK.register(MtrPlatformDoorClientIntegration::refreshPending);
    }

    private static void refreshPending(Minecraft client) {
        if (client.level != lastWorld) {
            lastWorld = client.level;
            nextRefreshTick = 0;
        }
        pending.removeIf(door -> door.isRemoved() || door.getLevel() != client.level);
        if (client.level == null || pending.isEmpty() || client.level.getGameTime() < nextRefreshTick) {
            return;
        }
        MinecraftClientData data = MinecraftClientData.getInstance();
        if (data == null) {
            return;
        }

        // One vehicle scan per batch of doors, rather than one scan for every door.
        for (BlockEntityPlatformDoor door : pending) {
            door.setExternalOpenValue(0f);
        }
        boolean[] ready = {true};
        for (var vehicle : data.vehicles) {
            var cars = vehicle.getVehicleCarsAndPositions();
            for (int i = 0; i < cars.size(); i++) {
                var car = cars.get(i);
                ObjectArrayList<PositionAndRotation> bogies = new ObjectArrayList<>();
                for (var positions : car.right()) {
                    bogies.add(new PositionAndRotation(positions.left(), positions.right(), true));
                }
                if (bogies.isEmpty()) {
                    ready[0] = false;
                    continue;
                }
                var mode = vehicle.getTransportMode();
                PositionAndRotation transform = new PositionAndRotation(
                        bogies, car.left(), mode.hasPitchAscending || mode.hasPitchDescending);
                int carIndex = i;
                boolean[] found = {false};
                CustomResourceLoader.getVehicleById(mode, car.left().getVehicleId(), resource -> {
                    var cached = resource.left().getCachedVehicleResource(carIndex, cars.size(), false);
                    if (cached != null) {
                        found[0] = true;
                        for (var doorway : cached.doorways) {
                            // Reuse MTR's exact doorway detection and our existing signal injection.
                            RenderVehicleHelper.canOpenDoors(doorway, transform, vehicle.persistentVehicleData.getDoorValue() * 2);
                        }
                    }
                });
                ready[0] &= found[0];
            }
        }
        if (ready[0]) {
            pending.clear();
            nextRefreshTick = 0;
        } else {
            // Resource packs can finish loading later; avoid rescanning every tick while waiting.
            nextRefreshTick = client.level.getGameTime() + 20;
        }
    }
}
