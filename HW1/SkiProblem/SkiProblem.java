package SkiProblem;

import Problem.*;
import Search.Result;
import Search.SearchMethod;
import Search.SearchSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkiProblem implements Problem<SkiState, SkiAction> {
    final int mapWidth;
    final int mapHeight;
    final List<List<Integer>> map;
    final Coordinate startCoordinate;
    final int stamina;
    final List<Coordinate> lodgeCoordinates;
    int curDestIdx;
    SearchMethod searchMethod = null;

    public SkiProblem(final int mapWidth, final int mapHeight, List<List<Integer>> map, final Coordinate startCoordinate, final int stamina, final List<Coordinate> lodgeCoordinates) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.map = map;
        this.startCoordinate = startCoordinate;
        this.stamina = stamina;
        this.lodgeCoordinates = lodgeCoordinates;
        this.curDestIdx = 0;
    }

    @Override
    public SkiState initialState() {
        return new SkiState(startCoordinate);
    }

    @Override
    public int calculateCost(SkiState state, SkiAction action) {
        switch (searchMethod) {
            case BFS: {
                return 1;       // every move cost exactly one
            }

            case UNIFORM_COST_SEARCH: {
                return -2;
            }

            case A_STAR_SEARCH: {
                return -3;
            }

            default: {
                return -1;
            }
        }
    }

    private boolean isCoordinateValid(final Coordinate coordinate) {
        return !(
                coordinate.x < 0            ||
                coordinate.x >= mapWidth    ||
                coordinate.y < 0            ||
                coordinate.y >= mapHeight
        );
    }

    private boolean isActionValid(final SkiState state, final SkiAction action) {
        final Coordinate oldCoord = state.coordinate;
        final Coordinate newCoord = action.performAction(state.coordinate);
        if (!isCoordinateValid(newCoord)) {
            return false;
        }

        final int startElevation = Math.abs(map.get(oldCoord.y).get(oldCoord.x));
        final int endElevation = map.get(newCoord.y).get(newCoord.x);

        if (endElevation < 0 && startElevation < Math.abs(endElevation)) {
            // meet a tree that is not crossable
            return false;
        } else if (startElevation + stamina < endElevation) {
            // meet a ground that is too high
            return false;
        } else {
            return true;
        }

    }

    @Override
    public List<SkiAction> validActions(SkiState state) {
        return SkiAction.ALL_ACTIONS.stream()
                                    .filter(skiAction -> isActionValid(state, skiAction))
                                    .collect(Collectors.toList());
    }

    @Override
    public boolean isGoalMeet(SkiState state) {
        return state.coordinate.equals(lodgeCoordinates.get(curDestIdx));
    }

    @Override
    public SkiState result(SkiState state, SkiAction action) {
        return new SkiState(action.performAction(state.coordinate));
    }

    public List<Result<SkiSolution>> solve(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
        final SearchSolver solver = this.searchMethod.solver();
        this.curDestIdx = 0;
        final SkiSolutionGenerator skiSolutionGenerator = new SkiSolutionGenerator();
        ArrayList<Result<SkiSolution>> answers = new ArrayList<>();

        for (; curDestIdx < lodgeCoordinates.size(); curDestIdx++) {
            assert solver != null;
            answers.add(solver.solve(this, skiSolutionGenerator));
        }

        return answers;
    }
}
