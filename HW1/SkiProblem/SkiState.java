package SkiProblem;

import Problem.State;

public class SkiState extends State {
    public final Coordinate coordinate;

    public SkiState(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public int hashCode() {
        return coordinate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkiState)) return false;
        SkiState skiState = (SkiState) o;
        return coordinate.equals(skiState.coordinate);
    }

    @Override
    public String toString() {
        return coordinate.toString();
    }
}
