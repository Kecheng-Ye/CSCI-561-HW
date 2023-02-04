package SkiProblem;

import Problem.*;
import Search.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkiProblem implements Problem<SkiState, SkiAction> {
    final int mapWidth;
    final int mapHeight;
    final List<List<Integer>> map;
    final Coordinate startCoordinate;
    final int stamina;
    List<Coordinate> lodgeCoordinates;
    int curDestIdx;
    SearchMethod searchMethod = null;
    private static int VERTICAL_OR_HORIZONTAL_MOVE_COST = 10;
    private static int DIANGOL_MOVE_COST = 14;

    public final Heuristic DiagnaolWalking = state -> {
        assert state instanceof SkiState;
        final SkiState skiState = (SkiState) state;
        
        final int horizontalDist = Math.abs(skiState.curCoordinate.x - lodgeCoordinates.get(curDestIdx).x);
        final int verticalDist = Math.abs(skiState.curCoordinate.y - lodgeCoordinates.get(curDestIdx).y);

        return Math.min(horizontalDist, verticalDist) * DIANGOL_MOVE_COST + Math.abs(horizontalDist - verticalDist) * VERTICAL_OR_HORIZONTAL_MOVE_COST;
    };

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
        return new SkiState(startCoordinate, startCoordinate, (searchMethod == SearchMethod.A_STAR_SEARCH));
    }

    private static int calculateHorizontalMoveCost(SkiAction action) {
        if (SkiAction.verticalOrHorizontalMove.contains(action.direction)) {
            return VERTICAL_OR_HORIZONTAL_MOVE_COST;
        } else if (SkiAction.DiagonalMove.contains(action.direction)) {
            return DIANGOL_MOVE_COST;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int calculateElevationChangeCost(SkiState state, SkiAction action) {
        final Coordinate prevCoord = state.prevCoordinate;
        final Coordinate curCoord = state.curCoordinate;
        final Coordinate nextCoord = action.performAction(state.curCoordinate);

        final int E_prev = Math.abs(map.get(prevCoord.y).get(prevCoord.x));
        final int E_curr = Math.abs(map.get(curCoord.y).get(curCoord.x));
        final int E_next = map.get(nextCoord.y).get(nextCoord.x);
        final int momentum = ((E_next - E_curr) > 0) ? Math.max(0, E_prev - E_curr) : 0;

        return ((E_next - E_curr) <= momentum) ? 0 : Math.max(0, E_next - E_curr - momentum);
    }

    private int calculateCostAStar(SkiState state, SkiAction action) {
        int horizontalMoveDistance = calculateHorizontalMoveCost(action);
        int elevationChangeCost = calculateElevationChangeCost(state, action);

        return horizontalMoveDistance + elevationChangeCost;
    }

    @Override
    public int calculateCost(SkiState state, SkiAction action) {
        switch (searchMethod) {
            case BFS: {
                return 1;       // every move cost exactly one
            }

            case UNIFORM_COST_SEARCH: {
                return calculateHorizontalMoveCost(action);
            }

            case A_STAR_SEARCH: {
                return calculateCostAStar(state, action);
            }

            default: {
                throw new RuntimeException();
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

    private boolean isMoveValid_BFS_UCS(final Coordinate oldCoord, final Coordinate newCoord) {
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

    private boolean isMoveValid_A_STAR(final Coordinate prevCoord, final Coordinate curCoord, final Coordinate nextCoord) {
        final int E_prev = Math.abs(map.get(prevCoord.y).get(prevCoord.x));
        final int E_curr = Math.abs(map.get(curCoord.y).get(curCoord.x));
        final int E_next = map.get(nextCoord.y).get(nextCoord.x);

        if (E_next < 0) {
            return E_curr >= Math.abs(E_next);
        }

        final int momentum = ((E_next - E_curr) > 0) ? Math.max(0, E_prev - E_curr) : 0;

        return E_curr + momentum + stamina >= E_next;
    }

    private boolean isActionValid(final SkiState state, final SkiAction action) {
        final Coordinate prevCoord = state.prevCoordinate;
        final Coordinate curCoord = state.curCoordinate;
        final Coordinate nextCoord = action.performAction(state.curCoordinate);
        if (!isCoordinateValid(nextCoord)) {
            return false;
        }

        if (searchMethod == SearchMethod.BFS || searchMethod == SearchMethod.UNIFORM_COST_SEARCH) {
            return isMoveValid_BFS_UCS(curCoord, nextCoord);
        } else if (searchMethod == SearchMethod.A_STAR_SEARCH) {
            return isMoveValid_A_STAR(prevCoord, curCoord, nextCoord);
        }

        return false;
    }

    @Override
    public List<SkiAction> validActions(SkiState state) {
        return SkiAction.ALL_ACTIONS.stream()
                                    .filter(skiAction -> isActionValid(state, skiAction))
                                    .collect(Collectors.toList());
    }

    @Override
    public boolean isGoalMeet(SkiState state) {
        return state.curCoordinate.equals(lodgeCoordinates.get(curDestIdx));
    }

    @Override
    public SkiState result(SkiState state, SkiAction action) {
        return new SkiState(state.curCoordinate, action.performAction(state.curCoordinate), (searchMethod == SearchMethod.A_STAR_SEARCH));
    }

    public List<Result<SkiSolution>> solve(SearchMethod searchMethod) {
        this.searchMethod = searchMethod;
        final SearchSolver solver = this.searchMethod.solver();

        assert solver != null;
        if (searchMethod == SearchMethod.A_STAR_SEARCH) {
            ((AStarSearch)solver).setHeuristic(DiagnaolWalking);
        }

        this.curDestIdx = 0;
        final SkiSolutionGenerator skiSolutionGenerator = new SkiSolutionGenerator();
        ArrayList<Result<SkiSolution>> answers = new ArrayList<>();

        for (; curDestIdx < lodgeCoordinates.size(); curDestIdx++) {
            answers.add(solver.solve(this, skiSolutionGenerator));
        }

        return answers;
    }
}
