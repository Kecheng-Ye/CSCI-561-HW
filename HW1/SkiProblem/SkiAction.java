package SkiProblem;

import Problem.Action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class SkiAction extends Action {
    public enum Direction {
        North,
        South,
        West,
        East,
        NorthEast,
        NorthWest,
        SouthEast,
        SouthWest,
    }

    public final Direction direction;
    public static final List<SkiAction> ALL_ACTIONS = Arrays.stream(Direction.values()).map(dir -> new SkiAction(dir)).collect(Collectors.toList());
    public static final HashSet<Direction> verticalOrHorizontalMove = new HashSet<>(List.of(Direction.South, Direction.East, Direction.West, Direction.North));
    public static final HashSet<Direction> DiagonalMove = new HashSet<>(List.of(Direction.NorthEast, Direction.NorthWest, Direction.SouthEast, Direction.SouthWest));

    public SkiAction(Direction direction) {
        this.direction = direction;
    }

    public Coordinate performAction(Coordinate oldCoord) {
        switch (this.direction) {
            case North: {
                return new Coordinate(oldCoord.x, oldCoord.y - 1);
            }

            case South: {
                return new Coordinate(oldCoord.x, oldCoord.y + 1);
            }

            case East: {
                return new Coordinate(oldCoord.x + 1, oldCoord.y);
            }

            case West: {
                return new Coordinate(oldCoord.x - 1, oldCoord.y);
            }

            case NorthEast: {
                return new Coordinate(oldCoord.x + 1, oldCoord.y - 1);
            }

            case NorthWest: {
                return new Coordinate(oldCoord.x - 1, oldCoord.y - 1);
            }

            case SouthEast: {
                return new Coordinate(oldCoord.x + 1, oldCoord.y + 1);
            }

            case SouthWest: {
                return new Coordinate(oldCoord.x - 1, oldCoord.y + 1);
            }

            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
