package models;

import java.util.Objects;

/**
 * Класс, представляющий координаты объекта.
 * @author kifmu
 */
public class Coordinates {
    private Float x;
    private Double y;

    public Coordinates(Float x, Double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates coordinates = (Coordinates) o;
        return Objects.equals(x, coordinates.x) && Objects.equals(y, coordinates.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString(){
        return "x: " + x + ", y: " + y;
    }
}