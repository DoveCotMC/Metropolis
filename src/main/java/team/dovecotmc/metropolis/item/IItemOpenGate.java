package team.dovecotmc.metropolis.item;

public interface IItemOpenGate {
    default boolean exitOnly() {
        return true;
    }
}
