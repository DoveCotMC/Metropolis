package team.dovecotmc.old.metropolis.mtr;

public class WrappedMtrStation {
    public final String name;
    public final int color;
    public final int zone;

    public WrappedMtrStation(String name, int color, int zone) {
        this.name = name;
        this.color = color;
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getZone() {
        return zone;
    }
}
