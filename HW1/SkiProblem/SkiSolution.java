package SkiProblem;

import Search.Solution;

import java.util.List;

public class SkiSolution extends Solution {
    List<Coordinate> stepCoords;
    public long pathCost;

    public SkiSolution(final List<Coordinate> answer, final long pathCost) {
        stepCoords = answer;
        this.pathCost = pathCost;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stepCoords.forEach(coordinate -> stringBuilder.append(coordinate.toString()).append(" "));
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
