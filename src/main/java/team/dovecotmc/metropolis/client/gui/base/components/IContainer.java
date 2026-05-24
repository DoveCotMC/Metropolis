package team.dovecotmc.metropolis.client.gui.base.components;

import org.jetbrains.annotations.Nullable;
import team.dovecotmc.metropolis.client.gui.base.BaseMetropolisScreen;

public interface IContainer {
    void addComponent(ComponentBase component);

    float getWidth();

    float getHeight();

    @Nullable IContainer getParent();

    BaseMetropolisScreen getRoot();
}
