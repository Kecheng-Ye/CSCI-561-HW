package PenteGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PenteGameBoardUtil {
    public static class PenteGameTerminationStatus {
        public final boolean isFinished;
        public final PenteGamePiece winner;

        private PenteGameTerminationStatus(final boolean isFinished, final PenteGamePiece winner) {
            this.isFinished = isFinished;
            this.winner = winner;
        }

        public static PenteGameTerminationStatus success(final PenteGamePiece winner) {
            return new PenteGameTerminationStatus(true, winner);
        }

        public static PenteGameTerminationStatus fail() {
            return new PenteGameTerminationStatus(false, null);
        }
    }

    private static final int NUM_OF_DIRECTIONS = 4;

    private static final int[][] directions = {
            {0, 1},         // horizontal: left to right
            {1, 0},         // vertical: top to bottom
            {1, 1},         // diagonal: top left to bottom right
            {1, -1}         // diagonal: top right to bottom left
    };

    private static final List<PenteGameCoordinate> horizontalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            add(PenteGameCoordinate.getCoordinate(i, 0));
        }
    }};

    private static final List<PenteGameCoordinate> verticalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < PenteGame.BOARD_WIDTH; i++) {
            add(PenteGameCoordinate.getCoordinate(0, i));
        }
    }};

    private static final List<PenteGameCoordinate> leftDiagonalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < (PenteGame.BOARD_HEIGHT + PenteGame.BOARD_WIDTH - 1); i++) {
            add(PenteGameCoordinate.getCoordinate(Math.max(0, 18 - i), Math.max(0, i - 18)));
        }
    }};

    private static final List<PenteGameCoordinate> rightDiagonalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < (PenteGame.BOARD_HEIGHT + PenteGame.BOARD_WIDTH - 1); i++) {
            add(PenteGameCoordinate.getCoordinate(Math.max(0, i - 18), Math.min(18, i)));
        }
    }};

    private static final List<List<PenteGameCoordinate>> startPoints = List.of(
            horizontalStartPoint,
            verticalStartPoint,
            leftDiagonalStartPoint,
            rightDiagonalStartPoint
    );

    static final int MAX_NUM_PIECES_IN_A_ROW = 5;

    private static PenteGameTerminationStatus checkIsFinishedOneDimension(
            final PenteGamePiece[][] board,
            final PenteGameCoordinate startCoordinate,
            final int[] moveDirection
    ) {
        int samePieceCount = 0;
        PenteGamePiece currPiece = null;
        int currX = startCoordinate.x;
        int currY = startCoordinate.y;

        while (PenteGameCoordinate.isCoordinateValid(currY, currX)) {
            if (board[currY][currX] == null) {
                samePieceCount = 0;
                currPiece = null;
            } else {
                if (currPiece == board[currY][currX]) {
                    samePieceCount++;
                    if (samePieceCount >= MAX_NUM_PIECES_IN_A_ROW) {
                        return PenteGameTerminationStatus.success(currPiece);
                    }
                } else {
                    samePieceCount = 1;
                    currPiece = board[currY][currX];
                }
            }

            currY += moveDirection[0];
            currX += moveDirection[1];
        }

        return PenteGameTerminationStatus.fail();
    }

    private static PenteGamePiece[][] prevCheckBoard = null;
    private static PenteGameTerminationStatus prevCheckResult = null;

    public static PenteGameTerminationStatus checkGameIsFinished(final PenteGamePiece[][] board) {
        // We often call Utility after isTerminal
        // this mechanism can prevent redundant checking
        if (Arrays.deepEquals(board, prevCheckBoard)) {
            assert prevCheckResult != null;
            return prevCheckResult;
        }

        prevCheckBoard = board;
        for (int i = 0; i < NUM_OF_DIRECTIONS; i++) {
            final int[] directionMove = directions[i];
            final List<PenteGameCoordinate> directionStartPoints = startPoints.get(i);

            for (final PenteGameCoordinate startPoint : directionStartPoints) {
                final PenteGameTerminationStatus status = checkIsFinishedOneDimension(board, startPoint, directionMove);
                if (status.isFinished) {
                    prevCheckResult = status;
                    return status;
                }
            }
        }

        prevCheckResult = PenteGameTerminationStatus.fail();
        return prevCheckResult;
    }

    public static PenteGameState placePieceOnBoard(final PenteGameState prevState, final PenteGameAction action) {
        PenteGameState newState =  PenteGameState.fromPrevState(prevState);

        // place the piece
        final PenteGameCoordinate coordinate = action.coordinate;
        newState.board[coordinate.y][coordinate.x] = action.pieceColor;

        // make potential capture
        tryToCapture(newState, coordinate);

        return newState;
    }

    private static void tryToCapture(final PenteGameState newState, final PenteGameCoordinate pieceCoordinate) {
        for (final int[] direction : directions) {
            tryToCaptureOneDirection(newState, pieceCoordinate, direction);
            tryToCaptureOneDirection(newState, pieceCoordinate, new int[] {-1 * direction[0], -1 * direction[1]});
        }
    }

    private static final int CAPTURE_RANGE = 4;
    private static final PenteGamePiece[] captureCandidate = new PenteGamePiece[CAPTURE_RANGE];

    private static void tryToCaptureOneDirection(final PenteGameState newState, final PenteGameCoordinate pieceCoordinate, final int[] direction) {
        for (int i = 0; i < CAPTURE_RANGE; i++) {
            int currY = pieceCoordinate.y + i * direction[0];
            int currX = pieceCoordinate.x + i * direction[1];

            if (!PenteGameCoordinate.isCoordinateValid(currY, currX)) {
                return;
            }
            captureCandidate[i] = newState.board[currY][currX];
        }

        boolean captureTwoEnd = captureCandidate[0] == captureCandidate[3];
        boolean captureContent = (captureCandidate[1] == captureCandidate[2]) && (captureCandidate[1] != null);
        boolean captureOpponentRelation = captureCandidate[0] != captureCandidate[1];

        if (captureTwoEnd && captureContent && captureOpponentRelation) {
            newState.board[pieceCoordinate.y + direction[0]][pieceCoordinate.x + direction[1]] = null;
            newState.board[pieceCoordinate.y + 2 * direction[0]][pieceCoordinate.x + 2 * direction[1]] = null;

            if (captureCandidate[0] == PenteGamePiece.BLACK) {
                newState.blackCaptures++;
            } else{
                newState.whiteCaptures++;
            }
        }
    }
}
