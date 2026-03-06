package team.dovecotmc.old.metropolis.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mtr.core.data.Station;
import org.mtr.core.simulation.Simulator;
import org.mtr.mapping.holder.World;
import org.mtr.mod.Init;
import org.mtr.mod.client.MinecraftClientData;
import team.dovecotmc.old.metropolis.mixins.accessor.AccessorMTRInit;
import team.dovecotmc.old.metropolis.mixins.accessor.AccessorMTRMain;
import team.dovecotmc.old.metropolis.mtr.WrappedMtrStation;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<WrappedMtrStation> getStations(Level level) {
//        if (world.isClientSide()) {
//            return ClientData.STATIONS;
//        }
//        return RailwayData.getInstance(world).stations;
        if (level.isClientSide()) {
            MinecraftClientData clientData = MinecraftClientData.getInstance();
            if (clientData != null) {
                Set<WrappedMtrStation> stations = new HashSet<>();
                for (Station station : clientData.stations) {
                    stations.add(new WrappedMtrStation(station.getName(), station.getColor(), (int) station.getZone1()));
                }
                return stations;
            }
        } else {
            World world = new World(level);
            for (Simulator simulator : ((AccessorMTRMain) AccessorMTRInit.getMain()).getSimulators()) {
                if (simulator.dimension.equals(Init.getWorldId(world))) {
                    Set<WrappedMtrStation> stations = new HashSet<>();
                    for (Station station : simulator.stations) {
                        stations.add(new WrappedMtrStation(station.getName(), station.getColor(), (int) station.getZone1()));
                    }
                    return stations;
                }
            }
        }

        return new HashSet<>();
    }

    public static WrappedMtrStation getStationByPos(BlockPos pos, Level level) {
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
                        return new WrappedMtrStation(station.getName(), station.getColor(), (int) station.getZone1());
                    }
                }
            }
        } else {
            World world = new World(level);
            for (Simulator simulator : ((AccessorMTRMain) AccessorMTRInit.getMain()).getSimulators()) {
                if (simulator.dimension.equals(Init.getWorldId(world))) {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    for (Station station : simulator.stations) {
                        if (
                                station.getMinX() <= x && x <= station.getMaxX() &&
                                        station.getMinY() <= y && y <= station.getMaxY() &&
                                        station.getMinZ() <= z && z <= station.getMaxZ()
                        ) {
                            return new WrappedMtrStation(station.getName(), station.getColor(), (int) station.getZone1());
                        }
                    }
                    break;
                }
            }
        }
        return null;
    }
}
