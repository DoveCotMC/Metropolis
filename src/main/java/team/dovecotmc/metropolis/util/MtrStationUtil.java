package team.dovecotmc.metropolis.util;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.mtr.core.Main;
import org.mtr.core.data.Station;
import org.mtr.core.map.UpdateDynmap;
import org.mtr.core.operation.NearbyAreasRequest;
import org.mtr.core.servlet.OperationProcessor;
import org.mtr.core.simulation.Simulator;
import org.mtr.init.MTR;
import org.mtr.init.MTRClient;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockTicketBarrier;
import org.mtr.mod.client.MinecraftClientData;
import team.dovecotmc.metropolis.mixins.AccessorMtrMain;

import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    @Deprecated
    public static Set<Station> getStations(Level world) {
        if (world.isClientSide()) {
            return MinecraftClientData.getInstance().stations;
        }
        return null;
//        return AccessorMtrMain.getSimulators().get;
    }

    @Deprecated
    public static Station getStationByPos(BlockPos pos, Level world) {
//        if (world.isClientSide()) {
//            return RailwayData.getStation(getStations(world), ClientData.DATA_CACHE, pos);
//        }
//        return RailwayData.getStation(getStations(world), RailwayData.getInstance(world).dataCache, pos);
        return null;
    }
}
