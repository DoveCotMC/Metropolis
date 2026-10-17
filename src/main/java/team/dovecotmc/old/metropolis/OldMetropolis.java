package team.dovecotmc.old.metropolis;

import net.fabricmc.loader.api.FabricLoader;
import team.dovecotmc.metropolis.Metropolis;
import team.dovecotmc.old.metropolis.init.OldMetroBlocks;
import team.dovecotmc.old.metropolis.init.OldMetroItems;
import team.dovecotmc.old.metropolis.mtr.MtrPlatformDoorIntegration;
import team.dovecotmc.old.metropolis.network.OldMetroServerNetwork;

public class OldMetropolis {
    public static final boolean ADD_ITEMS_TO_TAB = true;
    private static boolean enableMtrContent = false;

    public static void initializeOldContent() {
        enableMtrContent = FabricLoader.getInstance().isModLoaded("mtr");
        if (enableMtrContent) {
            Metropolis.LOGGER.info("MTR 4 detected! Loading legacy content.");
            OldMetroServerNetwork.registerAll();
            OldMetroBlocks.initialize();
            OldMetroItems.initialize();
            MtrPlatformDoorIntegration.initialize();
        } else {
            Metropolis.LOGGER.info("MTR 4 not detected! Skipped legacy content.");
        }
    }

    public static boolean shouldEnableMtrContent() {
        return enableMtrContent;
    }

    public static boolean shouldAddItemsToTab() {
        return ADD_ITEMS_TO_TAB && enableMtrContent;
    }
}
