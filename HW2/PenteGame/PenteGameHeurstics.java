package PenteGame;

import MinMaxSearchSolver.Heurstics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PenteGameHeurstics implements Heurstics<PenteGameState, PenteGamePlayer> {
    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public float eval(final PenteGameState state, PenteGamePlayer self) {
        PenteGamePlayer opponent = PenteGamePlayer.getOpponent(self);

        final float captureScore = captureScore(state, self) - captureScore(state, opponent);
        final float connectedComponentScore = connectedComponentScore(state, self) - connectedComponentScore(state, opponent);
        final float consecutivePieceScore = consecutivePieceScore(state, self) - consecutivePieceScore(state, opponent);

        final float score = captureScore * 0.33f + connectedComponentScore * 0.33f + consecutivePieceScore * 0.33f;
        // final float score = captureScore * 0.33f + connectedComponentScore * 0.33f;
        return clamp(score, -1f, 1f);
    }

    private float connectedComponentScore(final PenteGameState state, final PenteGamePlayer player) {
        final boolean[][] isVisited = new boolean[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
        for (boolean[] row: isVisited)
            Arrays.fill(row, false);

        int totalSamePieceIslandArea = 0;
        int numOfComponents = 0;

        for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator(); it.hasNext(); ) {
            PenteGameCoordinate coordinate = it.next();
            final int y = coordinate.y;
            final int x = coordinate.x;

            if ((state.board.get(y, x) != player.playerType) || isVisited[y][x]) continue;

            totalSamePieceIslandArea += DFS(state.board, coordinate, isVisited);
            numOfComponents += 1;
        }

        if (numOfComponents == 0f) return 0;

        final float numOfComponentsScore = Math.min(2 / (float)numOfComponents, 1);
        final float eachComponentQualityScore = (float)totalSamePieceIslandArea / numOfComponents;
        final float connectedComponentScore = numOfComponentsScore * eachComponentQualityScore * (float)0.2;

        return clamp(connectedComponentScore, -1f, 1f);
    }

    private int DFS(
            final PenteGameBoard board,
            final PenteGameCoordinate coordinate,
            final boolean[][] isVisited) {
        assert board.get(coordinate) != null;
        isVisited[coordinate.y][coordinate.x] = true;
        int result = 1;

        final PenteGamePiece current = board.get(coordinate);

        for (final int[] direction : PenteGameBoardUtil.directions) {
            int newY = coordinate.y + direction[0];
            int newX = coordinate.x + direction[1];

            if (shouldContinueDFS(board, current, newY, newX, isVisited)) {
                result += DFS(board, PenteGameCoordinate.getCoordinate(newY, newX), isVisited);
            }

            newY = coordinate.y - direction[0];
            newX = coordinate.x - direction[1];

            if (shouldContinueDFS(board, current, newY, newX, isVisited)) {
                result += DFS(board, PenteGameCoordinate.getCoordinate(newY, newX), isVisited);
            }
        }

        return result;
    }

    private boolean shouldContinueDFS(final PenteGameBoard board, final PenteGamePiece targetPiece, int newY, int newX, final boolean[][] isVisited) {
        return  PenteGameCoordinate.isCoordinateValid(newY, newX) &&
                (!isVisited[newY][newX]) &&
                (board.get(newY, newX) == targetPiece);
    }

    private float captureScore(final PenteGameState state, final PenteGamePlayer player) {
        final int numOfCaptures = (player.playerType == PenteGamePiece.WHITE) ? state.whiteCaptures :
                                                                                state.blackCaptures;
        return ((float)numOfCaptures / PenteGame.NUM_OF_CAPTURES_TO_FINISH);
    }

    private float consecutivePieceScore(final PenteGameState state, final PenteGamePlayer player) {
        List<List<PenteGameCoordinate>> startPoints = List.of(
                PenteGameBoardUtil.horizontalStartPointWithAABB(state.board.leftTop, state.board.rightBottom),
                PenteGameBoardUtil.verticalStartPointWithAABB(state.board.leftTop, state.board.rightBottom),
                PenteGameBoardUtil.leftDiagonalStartPointWithAABB(state.board.leftTop, state.board.rightBottom),
                PenteGameBoardUtil.rightDiagonalStartPointWithAABB(state.board.leftTop, state.board.rightBottom)
        );

        float score = 0f;
        final int width = state.board.rightBottom.x - state.board.leftTop.x;
        final int height = state.board.rightBottom.y - state.board.leftTop.y;
        float factor = (Math.max(width, height) <= 5) ? (0.5f / Math.max(width, height)) : (2f / Math.max(width, height));

        for (int i = 0; i < PenteGameBoardUtil.NUM_OF_DIRECTIONS; i++) {
            final int[] directionMove = PenteGameBoardUtil.directions[i];
            final List<PenteGameCoordinate> directionStartPoints = startPoints.get(i);
            for (final PenteGameCoordinate startPoint : directionStartPoints) {
                score += consecutivePieceScoreOneDimension(state.board, startPoint, directionMove, player) * factor;
            }
        }

        return clamp(score, -1f, 1f);
    }

    private static class ConsecutivePiecesRange {
        public PenteGameCoordinate prevOpponentPiece;
        public PenteGameCoordinate nextOpponentPiece;
        public final PenteGameCoordinate start;
        public final PenteGameCoordinate end;
        public float length;

        // public ConsecutivePiecesRange(final PenteGameCoordinate start) {
        //     this.start = new PenteGameCoordinate(start);
        //     this.end = new PenteGameCoordinate(start);
        //     this.prevOpponentPiece = null;
        //     this.nextOpponentPiece = null;
        //     this.length = 1f;
        // }

        public ConsecutivePiecesRange(final PenteGameCoordinate start, final PenteGameCoordinate prevOpponentPiece) {
            this.start = new PenteGameCoordinate(start);
            this.end = new PenteGameCoordinate(start);
            this.prevOpponentPiece = (prevOpponentPiece == null) ? null : new PenteGameCoordinate(prevOpponentPiece);
            this.nextOpponentPiece = null;
            this.length = 1f;
        }

        public boolean enlarge(final PenteGameCoordinate newEnd, final int[] direction) {
            if (nextOpponentPiece != null) return false;

            int distance = calculateDist(this.end, newEnd, direction);
            assert distance >= 1;

            if (distance > 3) return false;

            this.end.y = newEnd.y;
            this.end.x = newEnd.x;
            this.length += (float)1 / distance;
            return true;
        }

        private static int calculateDist(final PenteGameCoordinate src, final PenteGameCoordinate dst, final int[] direction) {
            int yDiff = dst.y - src.y;
            int xDiff = dst.x - src.x;

            if (yDiff == 0) {
                return xDiff / direction[1];
            }

            return yDiff / direction[0];
        }

        public float calculateScore(final int[] moveDirection) {
            if (prevOpponentPiece == null && nextOpponentPiece == null) {
                return length;
            }

            int distFromPrevOpponent = (prevOpponentPiece != null) ? ConsecutivePiecesRange.calculateDist(prevOpponentPiece, start, moveDirection) : Integer.MAX_VALUE;
            int distFromCurrOpponent = (nextOpponentPiece != null) ? ConsecutivePiecesRange.calculateDist(end, nextOpponentPiece, moveDirection) : Integer.MAX_VALUE;
            assert distFromPrevOpponent >= 0 && distFromCurrOpponent >= 0;

            if (distFromPrevOpponent == 1 && distFromCurrOpponent == 1) {
                return 0;
            } else if (distFromPrevOpponent == 1 || distFromCurrOpponent == 1) {
                return length / 2;
            }

            return length;
        }
    }

    private static float consecutivePieceScoreOneDimension(
            final PenteGameBoard board,
            final PenteGameCoordinate startCoordinate,
            final int[] moveDirection,
            final PenteGamePlayer player
    ) {
        List<ConsecutivePiecesRange> ranges = new ArrayList<>();
        PenteGameCoordinate opponentPiece = null;

        PenteGameCoordinate temp = new PenteGameCoordinate(startCoordinate);

        while (temp.y <= board.rightBottom.y && temp.x <= board.rightBottom.x) {
            PenteGamePiece piece = board.get(temp);
            if (piece != null) {
                if (piece != player.playerType) {
                    opponentPiece = new PenteGameCoordinate(temp);
                    if (!ranges.isEmpty()) {
                        final ConsecutivePiecesRange lastRange = ranges.get(ranges.size() - 1);
                        lastRange.nextOpponentPiece = new PenteGameCoordinate(temp);
                    }
                } else {
                    if (ranges.isEmpty()) {
                        ranges.add(new ConsecutivePiecesRange(temp, opponentPiece));
                    } else {
                        final ConsecutivePiecesRange lastRange = ranges.get(ranges.size() - 1);
                        if (!lastRange.enlarge(temp, moveDirection)) {
                            ranges.add(new ConsecutivePiecesRange(temp, opponentPiece));
                        }
                    }
                }
            }

            temp.y += moveDirection[0];
            temp.x += moveDirection[1];
        }

        if (ranges.isEmpty()) return 0f;

        return ranges
                .stream()
                .reduce(0f, (prevScore, range) -> range.calculateScore(moveDirection) + prevScore, Float::sum) / PenteGame.MAX_NUM_PIECES_IN_A_ROW;
    }
}
