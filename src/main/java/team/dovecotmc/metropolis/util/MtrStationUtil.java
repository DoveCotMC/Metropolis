package team.dovecotmc.metropolis.util;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mtr.core.data.Station;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockTicketBarrier;

import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<Station> getStations(Level world) {
        Init.sendMessageC2S(OperationProcessor.NEARBY_STATIONS, world.getServer(), world, new NearbyAreasRequest<>(Init.blockPosToPosition(new org.mtr.mapping.holder.BlockPos(BlockPos.ZERO)), 0), response -> {
            final ObjectImmutableList<Station> stations = response.();
        });
        return Station.getInstance(world).stations;
    }

    public static Station getStationByPos(BlockPos pos, Level world) {
        if (world.isClientSide()) {
            return RailwayData.getStation(getStations(world), ClientData.DATA_CACHE, pos);
        }
        return RailwayData.getStation(getStations(world), RailwayData.getInstance(world).dataCache, pos);
    }
}
