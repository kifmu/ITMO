package models;

import java.util.Objects;

/**
 * Класс, представляющий локацию с координатами и названием.
 * @author kifmu
 */
public class Location {
    private Float x;
    private long y;
    private String name;

    public Location(Float locX, long locY, String locName) {
        this.x = locX;
        this.y = locY;
        this.name = locName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y &&
                Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, name);
    }

    @Override
    public String toString(){
        return "x: " + x + ", y: " + y + ", name: " + name;
    }
}