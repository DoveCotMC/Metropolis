package team.dovecotmc.metropolis.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mtr.core.data.Station;
import org.mtr.core.map.UpdateWebMap;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.operation.NearbyAreasResponse;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.World;
import org.mtr.mod.Init;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.screen.DashboardScreen;
import team.dovecotmc.metropolis.Metropolis;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<Station> getStations(Level world) {
//        if (world.isClientSide()) {
//            return ClientData.STATIONS;
//        }
//        return RailwayData.getInstance(world).stations;
        if (world.isClientSide()) {
            MinecraftClientData clientData = MinecraftClientData.getInstance();
            if (clientData != null) {
                return clientData.stations;
            }
        } else {
            // TODO: Seems like we don't need this anymore
            Metropolis.LOGGER.warn("MtrStationUtil.getStations was called on server although it was not implemented!");
        }

        return new HashSet<>();
    }

    public static Station getStationByPos(BlockPos pos, Level level) {
        if (level.isClientSide()) {
            MinecraftClientData clientData = MinecraftClientData.getInstance();
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            if (clientData != null) {
                for (Station station : clientData.stations) {
                    if (
                            station.getMinX() <= x && x <= station.getMaxX() &&
                            station.getMinY() <= y && y <= station.getMaxY() &&
                            station.getMinZ() <= z && z <= station.getMaxZ()
                    ) {
                        return station;
                    }
                }
            }
        } else {
            // TODO: Seems like we don't need this anymore
            Metropolis.LOGGER.warn("MtrStationUtil.getStationByPos was called on server although it was not implemented!");
        }
        return null;
    }
}
