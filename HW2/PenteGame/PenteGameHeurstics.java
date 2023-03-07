package PenteGame;

import MinMaxSearchSolver.Heurstics;

import java.util.ArrayList;
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

        final float score = captureScore * 0.35f + connectedComponentScore * 0.05f + consecutivePieceScore * 0.6f;
        return clamp(score, -1f, 1f);
    }

    private float connectedComponentScore(final PenteGameState state, final PenteGamePlayer player) {
        final boolean[][] isVisited = new boolean[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];

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
        final List<List<PenteGameCoordinate>> startPoints = PenteGameBoardUtil.startPointsBasedOnBoard(state.board);

        float maxScore1 = 0f;
        float maxScore2 = 0f;
        float maxScore3 = 0f;

        for (int i = 0; i < PenteGameBoardUtil.NUM_OF_DIRECTIONS; i++) {
            final int[] directionMove = PenteGameBoardUtil.directions[i];
            final List<PenteGameCoordinate> directionStartPoints = startPoints.get(i);
            for (final PenteGameCoordinate startPoint : directionStartPoints) {
                final float currScore = consecutivePieceScoreOneDimension(state.board, startPoint, directionMove, player);

                if (currScore >= maxScore1) {
                    maxScore3 = maxScore2;
                    maxScore2 = maxScore1;
                    maxScore1 = currScore;
                } else if (currScore >= maxScore2) {
                    maxScore3 = maxScore2;
                    maxScore2 = currScore;
                } else if (currScore >= maxScore3) {
                    maxScore3 = currScore;
                }
            }
        }

        return maxScore1 * 0.8f + maxScore2 * 0.15f + maxScore3 * 0.05f;
    }

    private static class ConsecutivePiecesRange {
        public PenteGameCoordinate prevOpponentPiece;
        public PenteGameCoordinate nextOpponentPiece;
        public PenteGameCoordinate start;
        public PenteGameCoordinate end;
        private float length;

        public ConsecutivePiecesRange(final PenteGameCoordinate start, final PenteGameCoordinate prevOpponentPiece) {
            this.start = start;
            this.end = start;
            this.prevOpponentPiece = prevOpponentPiece;
            this.nextOpponentPiece = null;
            this.length = 1f;
        }

        public boolean enlarge(final PenteGameCoordinate newEnd, final int[] direction) {
            if (nextOpponentPiece != null) return false;

            int distance = calculateDist(this.end, newEnd, direction);
            assert distance >= 1;

            if (distance > 3) return false;

            this.end = newEnd;
            this.length += (float)1 / distance;
            return true;
        }

        private static int calculateDist(final PenteGameCoordinate src, final PenteGameCoordinate dst, final int[] direction) {
            int yDiff = dst.y - src.y;
            int xDiff = dst.x - src.x;

            if (xDiff == 0 && yDiff == 0)
                return 0;

            if (yDiff == 0)
                return xDiff / direction[1];

            return yDiff / direction[0];
        }

        private static final float factor = (float) Math.pow(2, PenteGame.MAX_NUM_PIECES_IN_A_ROW - 1) + 1;

        public float calculateScore(final int[] moveDirection) {
            final float score = (float) Math.pow(2, length) / factor;

            if (prevOpponentPiece != null || nextOpponentPiece != null) {
                int distFromPrevOpponent = (prevOpponentPiece != null) ?
                        (ConsecutivePiecesRange.calculateDist(prevOpponentPiece, start, moveDirection) - 1) :
                        PenteGameBoardUtil.distToBoardBoundary(start, PenteGameBoardUtil.negativeDirection(moveDirection));
                int distFromNextOpponent = (nextOpponentPiece != null) ?
                        (ConsecutivePiecesRange.calculateDist(end, nextOpponentPiece, moveDirection) - 1) :
                        PenteGameBoardUtil.distToBoardBoundary(end, moveDirection);

                assert distFromPrevOpponent >= 0 && distFromNextOpponent >= 0;

                final int maxPotentialConsecutivePiece = calculateDist(start, end, moveDirection) + 1 + distFromPrevOpponent + distFromNextOpponent;

                if (distFromPrevOpponent == 0 && distFromNextOpponent == 0) {
                    return 0;
                } else if (maxPotentialConsecutivePiece < PenteGame.MAX_NUM_PIECES_IN_A_ROW) {
                    return 0;
                } else if (distFromPrevOpponent == 0 || distFromNextOpponent == 0) {
                    return score / 2f;
                }
            }

            return score;
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

        int currY = startCoordinate.y;
        int currX = startCoordinate.x;

        while (PenteGameBoardUtil.isWithInAABB(board.leftTop, board.rightBottom, currY, currX))
        {
            PenteGameCoordinate currCoordinate = PenteGameCoordinate.getCoordinate(currY, currX);
            PenteGamePiece piece = board.get(currY, currX);


            if (piece != null) {
                if (piece != player.playerType) {
                    opponentPiece = PenteGameCoordinate.getCoordinate(currY, currX);
                    if (!ranges.isEmpty()) {
                        final ConsecutivePiecesRange lastRange = ranges.get(ranges.size() - 1);
                        if (lastRange.nextOpponentPiece == null)
                            lastRange.nextOpponentPiece = currCoordinate;
                    }
                } else {
                    if (ranges.isEmpty()) {
                        ranges.add(new ConsecutivePiecesRange(currCoordinate, opponentPiece));
                    } else {
                        final ConsecutivePiecesRange lastRange = ranges.get(ranges.size() - 1);
                        if (!lastRange.enlarge(currCoordinate, moveDirection)) {
                            ranges.add(new ConsecutivePiecesRange(currCoordinate, opponentPiece));
                        }
                    }
                }
            }

            currY += moveDirection[0];
            currX += moveDirection[1];
        }

        if (ranges.isEmpty()) return 0f;

        return ranges
                .stream()
                .reduce(0f, (prevScore, range) -> range.calculateScore(moveDirection) + prevScore, Float::sum);
    }
}
