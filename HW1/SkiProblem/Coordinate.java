package SkiProblem;

import java.util.Objects;

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            Coordinate otherCoord = (Coordinate) obj;
            return (otherCoord.x == x && otherCoord.y == y);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return String.format("%d,%d", x, y);
    }
}
