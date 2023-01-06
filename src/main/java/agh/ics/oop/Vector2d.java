package agh.ics.oop;

import java.util.Objects;

public class Vector2d {

    public int x;
    public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(x, y);
    }

    boolean precedes(Vector2d other) {
        return x <= other.x && y <= other.y;
    }

    boolean follows(Vector2d other) {

        return x >= other.x && y >= other.y;
    }

    public Vector2d add(Vector2d other) {
        return new Vector2d(x + other.x, y + other.y);    //nowy obiekt naszej klasy, który jest sumą
    }

    public Vector2d subtract(Vector2d other) {

        return new Vector2d(x - other.x, y - other.y);
    }

    public Vector2d opposite() {

        return new Vector2d((-1) * x, (-1) * y);
    }

    public Vector2d upperRight(Vector2d other) {

        return new Vector2d(Math.max(x, other.x), Math.max(y, other.y));
    }

    public Vector2d lowerLeft(Vector2d other) {

        return new Vector2d(Math.min(x, other.x), Math.min(y, other.y));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
