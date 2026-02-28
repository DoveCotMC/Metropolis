package team.dovecotmc.metropolis.util;

import mtr.client.ClientData;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mtr.core.map.Station;
import org.mtr.core.simulation.Simulator;

import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<Station> getStations(Level world) {
        if (world.isClientSide()) {
            return ClientData.STATIONS;
        }
        return RailwayData.getInstance(world).stations;
    }

    public static org.mtr.core.map.Station getStationByPos(BlockPos pos, Level world) {
        if (world.isClientSide()) {
            Simulator.
            return RailwayData.getStation(getStations(world), ClientData.DATA_CACHE, pos);
        }
        return RailwayData.getStation(getStations(world), RailwayData.getInstance(world).dataCache, pos);
    }
}
