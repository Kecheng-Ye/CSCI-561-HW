package PenteGame;

import java.util.ArrayList;
import java.util.List;

public class PenteGameBoardUtil {
    public static final int[][] directions = {
            {0, 1},         // horizontal: left to right
            {1, 0},         // vertical: top to bottom
            {1, 1},         // diagonal: top left to bottom right
            {1, -1}         // diagonal: top right to bottom left
    };

    public static int[] negativeDirection(final int[] direction) {
        return new int[] {direction[0] * -1, direction[1] * -1};
    }

    public static final int NUM_OF_DIRECTIONS = directions.length;

    private static final List<PenteGameCoordinate> horizontalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            add(PenteGameCoordinate.getCoordinate(i, 0));
        }
    }};

    public static List<PenteGameCoordinate> horizontalStartPointWithAABB(final PenteGameCoordinate leftTop, final PenteGameCoordinate rightBottom) {
        int height = rightBottom.y - leftTop.y + 1;
        return new ArrayList<>() {{
            for (int i = 0; i < height; i++) {
                add(PenteGameCoordinate.getCoordinate(i + leftTop.y, leftTop.x));
            }
        }};
    }

    private static final List<PenteGameCoordinate> verticalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < PenteGame.BOARD_WIDTH; i++) {
            add(PenteGameCoordinate.getCoordinate(0, i));
        }
    }};

    public static List<PenteGameCoordinate> verticalStartPointWithAABB(final PenteGameCoordinate leftTop, final PenteGameCoordinate rightBottom) {
        int width = rightBottom.x - leftTop.x + 1;
        return new ArrayList<>() {{
            for (int i = 0; i < width; i++) {
                add(PenteGameCoordinate.getCoordinate(leftTop.y, i + leftTop.x));
            }
        }};
    }

    private static final List<PenteGameCoordinate> leftDiagonalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < (PenteGame.BOARD_HEIGHT + PenteGame.BOARD_WIDTH - 1); i++) {
            add(PenteGameCoordinate.getCoordinate(
                    Math.max(0, PenteGame.BOARD_HEIGHT - 1 - i),
                    Math.max(0, i - (PenteGame.BOARD_HEIGHT - 1)))
            );
        }
    }};

    public static List<PenteGameCoordinate> leftDiagonalStartPointWithAABB(final PenteGameCoordinate leftTop, final PenteGameCoordinate rightBottom) {
        int height = rightBottom.y - leftTop.y + 1;
        int width = rightBottom.x - leftTop.x + 1;
        return new ArrayList<>() {{
            for (int i = 0; i < (height + width - 1); i++) {
                add(PenteGameCoordinate.getCoordinate(
                        Math.max(0, height - 1 - i) + leftTop.y,
                        Math.max(0, i - (height - 1)) + leftTop.x)
                );
            }
        }};
    }

    private static final List<PenteGameCoordinate> rightDiagonalStartPoint = new ArrayList<>() {{
        for (int i = 0; i < (PenteGame.BOARD_HEIGHT + PenteGame.BOARD_WIDTH - 1); i++) {
            add(PenteGameCoordinate.getCoordinate(
                    Math.max(0, i - (PenteGame.BOARD_WIDTH - 1)),
                    Math.min(PenteGame.BOARD_WIDTH - 1, i))
            );
        }
    }};

    public static List<PenteGameCoordinate> rightDiagonalStartPointWithAABB(final PenteGameCoordinate leftTop, final PenteGameCoordinate rightBottom) {
        int height = rightBottom.y - leftTop.y + 1;
        int width = rightBottom.x - leftTop.x + 1;
        return new ArrayList<>() {{
            for (int i = 0; i < (height + width - 1); i++) {
                add(PenteGameCoordinate.getCoordinate(
                        Math.max(0, i - (width - 1)) + leftTop.y,
                        Math.min(width - 1, i) + leftTop.x)
                );
            }
        }};
    }

    private static final List<List<PenteGameCoordinate>> startPoints = List.of(
            horizontalStartPoint,
            verticalStartPoint,
            leftDiagonalStartPoint,
            rightDiagonalStartPoint
    );

    public static List<List<PenteGameCoordinate>> startPointsBasedOnBoard(final PenteGameBoard board) {
        return List.of(
                PenteGameBoardUtil.horizontalStartPointWithAABB(board.leftTop, board.rightBottom),
                PenteGameBoardUtil.verticalStartPointWithAABB(board.leftTop, board.rightBottom),
                PenteGameBoardUtil.leftDiagonalStartPointWithAABB(board.leftTop, board.rightBottom),
                PenteGameBoardUtil.rightDiagonalStartPointWithAABB(board.leftTop, board.rightBottom)
        );
    }

    private static PenteGameTerminationStatus checkBoardHasConsecutivePiecesOneDimension(
            final PenteGameBoard board,
            final PenteGameCoordinate startCoordinate,
            final int[] moveDirection
    ) {
        int samePieceCount = 0;
        PenteGamePiece currPiece = null;
        int currX = startCoordinate.x;
        int currY = startCoordinate.y;

        while (isWithInAABB(board.leftTop, board.rightBottom, currY, currX)) {
            if (board.get(currY, currX) == null) {
                samePieceCount = 0;
                currPiece = null;
            } else {
                if (currPiece == board.get(currY, currX)) {
                    samePieceCount++;
                    if (samePieceCount >= PenteGame.MAX_NUM_PIECES_IN_A_ROW) {
                        final PenteGamePlayer winner = (currPiece == PenteGamePiece.WHITE) ? PenteGamePlayer.WHITE_PLAYER : PenteGamePlayer.BLACK_PLAYER;
                        return PenteGameTerminationStatus.success(winner);
                    }
                } else {
                    samePieceCount = 1;
                    currPiece = board.get(currY, currX);
                }
            }

            currY += moveDirection[0];
            currX += moveDirection[1];
        }

        return PenteGameTerminationStatus.fail();
    }

    public static PenteGameTerminationStatus checkBoardHasConsecutivePieces(final PenteGameBoard board) {
        final List<List<PenteGameCoordinate>> currStartPoints = startPointsBasedOnBoard(board);

        for (int i = 0; i < NUM_OF_DIRECTIONS; i++) {
            final int[] directionMove = directions[i];
            final List<PenteGameCoordinate> directionStartPoints = currStartPoints.get(i);

            for (final PenteGameCoordinate startPoint : directionStartPoints) {
                final PenteGameTerminationStatus status = checkBoardHasConsecutivePiecesOneDimension(
                        board,
                        startPoint,
                        directionMove
                );

                if (status.isFinished) {
                    return status;
                }
            }
        }

        return PenteGameTerminationStatus.fail();
    }

    public static PenteGameState placePieceOnBoard(final PenteGameState prevState, final PenteGameAction action) {
        PenteGameState newState =  PenteGameState.fromPrevState(prevState);

        // place the piece
        final PenteGameCoordinate coordinate = action.coordinate;
        newState.board.put(coordinate, action.pieceColor);
        newState.numOfEmptySpots -= 1;

        // make potential capture
        tryToCapture(newState, coordinate);

        return newState;
    }

    private static void tryToCapture(final PenteGameState newState, final PenteGameCoordinate pieceCoordinate) {
        for (final int[] direction : directions) {
            tryToCaptureOneDirection(newState, pieceCoordinate, direction);
            tryToCaptureOneDirection(newState, pieceCoordinate, negativeDirection(direction));
        }
    }

    private static void tryToCaptureOneDirection(final PenteGameState newState, final PenteGameCoordinate pieceCoordinate, final int[] direction) {
        final PenteGamePiece[] captureCandidate = new PenteGamePiece[PenteGame.CAPTURE_RANGE];
        for (int i = 0; i < PenteGame.CAPTURE_RANGE; i++) {
            int currY = pieceCoordinate.y + i * direction[0];
            int currX = pieceCoordinate.x + i * direction[1];

            if (!PenteGameCoordinate.isCoordinateValid(currY, currX)) {
                return;
            }
            captureCandidate[i] = newState.board.get(currY, currX);
        }

        boolean captureTwoEnd = captureCandidate[0] == captureCandidate[3];
        boolean captureContent = (captureCandidate[1] == captureCandidate[2]) && (captureCandidate[1] != null);
        boolean captureOpponentRelation = captureCandidate[0] != captureCandidate[1];

        if (captureTwoEnd && captureContent && captureOpponentRelation) {
            newState.board.put(pieceCoordinate.y + direction[0], pieceCoordinate.x + direction[1], null);
            newState.board.put(pieceCoordinate.y + 2 * direction[0], pieceCoordinate.x + 2 * direction[1], null);
            newState.numOfEmptySpots += 2;

            if (captureCandidate[0] == PenteGamePiece.BLACK) {
                newState.blackCaptures++;
            } else{
                newState.whiteCaptures++;
            }
        }
    }

    public static PenteGameCoordinate[] getBoardAABB(final PenteGameBoard board) {
        PenteGameCoordinate leftTop = PenteGameCoordinate.leftTopInit;
        PenteGameCoordinate rightBottom = PenteGameCoordinate.rightBottomInit;

        for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator(); it.hasNext(); ) {
            PenteGameCoordinate coordinate = it.next();

            if (board.get(coordinate) == null) continue;

            leftTop = PenteGameCoordinate.getCoordinate(
                    Math.min(leftTop.y, coordinate.y),
                    Math.min(leftTop.x, coordinate.x)
            );

            rightBottom = PenteGameCoordinate.getCoordinate(
                    Math.max(rightBottom.y, coordinate.y),
                    Math.max(rightBottom.x, coordinate.x)
            );
        }

        return new PenteGameCoordinate[] {leftTop, rightBottom};
    }

    public static boolean outOfSquareRange(final PenteGameCoordinate center, final PenteGameCoordinate testPoint, final int radius) {
        return Math.abs(center.y - testPoint.y) >= radius ||
               Math.abs(center.x - testPoint.x) >= radius;
    }

    public static boolean withinSquareRange(
            final PenteGameCoordinate leftTop, final PenteGameCoordinate rightBottom,
            final PenteGameCoordinate testPoint,
            final int radius
    ) {
        return (leftTop.y - Math.min(testPoint.y, leftTop.y)) < radius             &&
               (leftTop.x - Math.min(testPoint.x, leftTop.x)) < radius             &&
               (Math.max(testPoint.y, rightBottom.y) - rightBottom.y) < radius     &&
               (Math.max(testPoint.x, rightBottom.x) - rightBottom.x) < radius;
    }

    public static boolean isWithInAABB(final PenteGameCoordinate leftTop, final PenteGameCoordinate rightBottom,
                                       final int currY, final int currX)
    {
        assert leftTop != PenteGameCoordinate.leftTopInit && rightBottom != PenteGameCoordinate.rightBottomInit;

        return currY >= leftTop.y && currX >= leftTop.x &&
               currY <= rightBottom.y && currX <= rightBottom.x;
    }

    public static int distToBoardBoundary(final PenteGameCoordinate point, final int[] moveDirection) {
        return Math.min(
                distToBoardBoundaryOneDimension(point.y, moveDirection[0], PenteGame.BOARD_HEIGHT),
                distToBoardBoundaryOneDimension(point.x, moveDirection[1], PenteGame.BOARD_WIDTH)
        );
    }

    private static int distToBoardBoundaryOneDimension(final int coordinate, final int direction, final int boundary) {
        if (direction == 0)
            return Integer.MAX_VALUE;

        if (direction < 0)
            return coordinate;

        return boundary - coordinate - 1;
    }

    public static String boardToStr(final PenteGamePiece[][] board) {
        StringBuilder builder = new StringBuilder();
        builder.append("    ");
        for (int j = 0; j < PenteGame.BOARD_WIDTH; j++) {
            builder.append(String.format("%c ", PenteGameCoordinate.rowNumToAlpha(j)));
        }
        builder.append("\n");

        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            builder.append(String.format("%3d ", (PenteGame.BOARD_HEIGHT - i)));
            for (int j = 0; j < PenteGame.BOARD_WIDTH; j++) {
                if (board[i][j] == null) {
                    builder.append('.');
                } else if (board[i][j] == PenteGamePiece.WHITE) {
                    builder.append('W');
                } else {
                    builder.append('B');
                }

                builder.append(' ');
            }
            builder.append('\n');
        }

        return builder.toString();
    }
}
