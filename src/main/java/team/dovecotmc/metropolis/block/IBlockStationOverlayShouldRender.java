package team.dovecotmc.metropolis.block;

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
