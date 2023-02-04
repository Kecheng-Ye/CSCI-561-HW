package SkiProblem;

import Problem.State;

public class SkiState extends State {
    public final Coordinate prevCoordinate;
    public final Coordinate curCoordinate;
    public final boolean isForAStarSearch;

    public SkiState(Coordinate prevCoordinate, Coordinate curCoordinate, boolean isForAStarSearch) {
        this.prevCoordinate = prevCoordinate;
        this.curCoordinate = curCoordinate;
        this.isForAStarSearch = isForAStarSearch;
    }

    @Override
    public int hashCode() {
        return curCoordinate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SkiState)) return false;
        SkiState skiState = (SkiState) o;

        if (isForAStarSearch) {
            return curCoordinate.equals(skiState.curCoordinate) && prevCoordinate.equals(skiState.prevCoordinate);
        } else {
            return curCoordinate.equals(skiState.curCoordinate);
        }
    }

    @Override
    public String toString() {
        return curCoordinate.toString();
    }
}
