package team.dovecotmc.metropolis.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.simulation.Simulator;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.World;
import org.mtr.mod.Init;

import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<org.mtr.core.map.Station> getStations(Level world) {
//        if (world.isClientSide()) {
//            return ClientData.STATIONS;
//        }
//        return RailwayData.getInstance(world).stations;
        return null;
    }

    public static org.mtr.core.map.Station getStationByPos(BlockPos pos, Level world) {
//        if (world.isClientSide()) {
//            return RailwayData.getStation(getStations(world), ClientData.DATA_CACHE, pos);
//        }
//        return RailwayData.getStation(getStations(world), RailwayData.getInstance(world).dataCache, pos);
        if (world.isClientSide()) {
//            OperationProcessor.NEARBY_STATIONS

//            Init.getWorldId()
//            Init.sendMessageC2S("nearby_stations", new MinecraftServer(world.getServer()), new World(world), new NearbyAreasRequest(Init.blockPosToPosition(new org.mtr.mapping.holder.BlockPos(pos)), 0L), (nearbyAreasResponse) -> {
//            });
        }
        return null;
    }
}
