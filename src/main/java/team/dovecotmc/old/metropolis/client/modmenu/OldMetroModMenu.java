package team.dovecotmc.old.metropolis.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public class OldMetroModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        try {
            Class<?> clazz = Class.forName("org.mtr.mod.Init");
            return (ConfigScreenFactory<OldMetroModMenuConfigScreen>) OldMetroModMenuConfigScreen::new;
        } catch (ClassNotFoundException e) {
            return screen -> null;
        }
    }
}
