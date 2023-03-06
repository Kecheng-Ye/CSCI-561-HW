package PenteGame;

import Game.State;

import java.util.Objects;

public class PenteGameState extends State {
    public final PenteGameBoard board;
    public final int round;
    public int whiteCaptures;
    public int blackCaptures;
    public int numOfEmptySpots;

    private PenteGameState(final PenteGameBoard board, final int round, final int whiteCaptures, final int blackCaptures, final int numOfEmptySpots) {
        this.board = new PenteGameBoard(board);
        this.round = round;
        this.whiteCaptures = whiteCaptures;
        this.blackCaptures = blackCaptures;
        this.numOfEmptySpots = numOfEmptySpots;
    }

    public static PenteGameState fromPrevState(final PenteGameState prevState) {
        return new PenteGameState(
                prevState.board,
                prevState.round + 1,
                prevState.whiteCaptures,
                prevState.blackCaptures,
                prevState.numOfEmptySpots
        );
    }

    public PenteGameState() {
        this.round = 0;
        this.board = new PenteGameBoard();
        this.whiteCaptures = 0;
        this.blackCaptures = 0;
        this.numOfEmptySpots = PenteGame.BOARD_HEIGHT * PenteGame.BOARD_WIDTH;
    }

    @Override
    public String toString() {
        return this.board.toString();
    }

    @Override
    public boolean equals(Object another) {
        if (another == this) {
            return true;
        }
        if (!(another instanceof PenteGameState)) {
            return false;
        }
        PenteGameState state = (PenteGameState) another;
        return this.board.equals(state.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.board.hashCode(), this.blackCaptures, this.whiteCaptures);
    }

    public static PenteGameState reconstructFromBoardAndCaptures(
            final PenteGameBoard board,
            final int whiteCapturePieces, final int blackCapturePieces
    ) {
        int numOfEmptySpots = 0;

        for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator(); it.hasNext(); ) {
            PenteGameCoordinate coordinate = it.next();
            numOfEmptySpots += (board.get(coordinate) == null ? 1 : 0);
        }

        int round = (PenteGame.BOARD_HEIGHT * PenteGame.BOARD_WIDTH) - numOfEmptySpots - whiteCapturePieces - blackCapturePieces;

        return new PenteGameState(board, round, whiteCapturePieces / 2, blackCapturePieces / 2, numOfEmptySpots);
    }
}
