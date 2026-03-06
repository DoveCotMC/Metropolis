package team.dovecotmc.old.metropolis;

import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.init.OldMetroBlocks;
import team.dovecotmc.old.metropolis.init.OldMetroItems;
import team.dovecotmc.old.metropolis.network.OldMetroServerNetwork;

public class OldMetropolis {
    public static final boolean ADD_ITEMS_TO_TAB = true;
    private static boolean enableMtrContent = false;

    public static void initializeOldContent() {
        boolean isMtr4Loaded;

        try {
            Class<?> clazz = Class.forName("org.mtr.mod.Init");
            isMtr4Loaded = true;
        } catch (ClassNotFoundException e) {
            isMtr4Loaded = false;
        }

        enableMtrContent = isMtr4Loaded;
        if (isMtr4Loaded) {
            Metropolis.LOGGER.info("MTR 4 detected! Loading legacy content.");
            OldMetroServerNetwork.registerAll();
            OldMetroBlocks.initialize();
            OldMetroItems.initialize();
        } else {
            Metropolis.LOGGER.info("MTR 4 not detected! Skipped legacy content.");
        }
    }

    public static boolean shouldAddItemsToTab() {
        return ADD_ITEMS_TO_TAB && enableMtrContent;
    }
}
