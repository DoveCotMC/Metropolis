package team.dovecotmc.metropolis.util;

import mtr.client.ClientData;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.world.World;

import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<Station> getStations(World world) {
        if (world.isClient()) {
            return ClientData.STATIONS;
        }
        return RailwayData.getInstance(world).stations;
    }
}
