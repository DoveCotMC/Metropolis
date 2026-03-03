package team.dovecotmc.old.metropolis.block;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
@SuppressWarnings("unused")
public interface IBlockStationOverlayShouldRender {
    default boolean shouldRenderName() {
        return true;
    }

    default boolean shouldRenderZone() {
        return false;
    }

    default boolean shouldRenderOutline() {
        return true;
    }
}
