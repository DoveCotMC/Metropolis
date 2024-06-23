package team.dovecotmc.metropolis.util;

import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.world.World;

import java.util.List;
import java.util.Set;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class MtrStationUtil {
    public static Set<Station> getAllStations(World world) {
        return RailwayData.getInstance(world).stations;
    }
}
