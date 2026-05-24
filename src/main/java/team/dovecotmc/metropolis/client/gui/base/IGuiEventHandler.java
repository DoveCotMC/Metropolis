package team.dovecotmc.metropolis.client.gui.base;

public interface IGuiEventHandler {
    void onMouseDown(int key);

    void onMouseRelease(int key);

    void onMouseEnter();

    void onMouseLeave();

    void onMouseScroll(double amount);
}
