package team.dovecotmc.metropolis.item;

/**
 * @author Arrokoth
 * @project Metropolis
 * @copyright Copyright © 2024 Arrokoth All Rights Reserved.
 */
public interface IItemOpenGate {
    default boolean exitOnly() {
        return true;
    }
}
