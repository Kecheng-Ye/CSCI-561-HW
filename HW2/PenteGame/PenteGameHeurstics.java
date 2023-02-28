package PenteGame;

import Game.BiPlayerGame;
import MinMaxSearchSolver.Heurstics;

import java.util.Arrays;

public class PenteGameHeurstics implements Heurstics<PenteGameState, PenteGamePlayer> {
    private static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    @Override
    public float eval(final PenteGameState state, PenteGamePlayer self) {
        PenteGamePlayer opponent = PenteGamePlayer.getOpponent(self);

        final float captureScore = captureScore(state, self) - captureScore(state, opponent);
        final float connectedComponentScore = connectedComponentScore(state, self) - connectedComponentScore(state, opponent);

        final float score = captureScore * 0.5f + connectedComponentScore * 0.5f;
        return clamp(score, -1f, 1f);
    }

    private float connectedComponentScore(final PenteGameState state, final PenteGamePlayer player) {
        final boolean[][] isVisited = new boolean[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
        for (boolean[] row: isVisited)
            Arrays.fill(row, false);

        float totalSamePieceIslandArea = 0f;
        float totalRectangluar = 0f;

        // centerness
        int maxHorizontalDist = -1;
        int maxVerticalDist = -1;
        final int midHorizontalPoint = PenteGame.BOARD_WIDTH / 2;
        final int midVerticalPoint = PenteGame.BOARD_HEIGHT / 2;

        for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator(); it.hasNext(); ) {
            PenteGameCoordinate coordinate = it.next();
            final int y = coordinate.y;
            final int x = coordinate.x;

            if (state.board.get(y, x) != player.playerType) continue;

            maxHorizontalDist = Math.max(maxHorizontalDist, Math.abs(midHorizontalPoint - x));
            maxVerticalDist = Math.max(maxVerticalDist, Math.abs(midVerticalPoint - y));

            if (isVisited[y][x]) continue;

            PenteGameCoordinate leftTop = new PenteGameCoordinate(coordinate);
            PenteGameCoordinate bottomRight = new PenteGameCoordinate(coordinate);
            float[] samePieceIslandArea = new float[]{0.0f};

            DFS(state.board, coordinate, leftTop, bottomRight, samePieceIslandArea, isVisited);

            totalSamePieceIslandArea += samePieceIslandArea[0];
            totalRectangluar += (bottomRight.y - leftTop.y + 1) * (bottomRight.x - leftTop.x + 1);
        }

        if (totalRectangluar == 0f) return 0;

        final float connectedComponentScore = Math.min(
                1.0f,
                (totalSamePieceIslandArea * 1.5f / totalRectangluar) * 2 * (totalRectangluar / PenteGame.MAX_NUM_PIECES_IN_A_ROW)
        );

        final float centernesScore = (float) (0.5 * (1 - (float)(maxHorizontalDist) / midHorizontalPoint) + // horizontal centerness
                                              0.5 * (1 - (float)(maxVerticalDist) / midVerticalPoint));     // vertical centerness

        return (float) (connectedComponentScore * 0.8 + centernesScore * 0.2);
    }

    private void DFS(
            final PenteGameBoard board,
            final PenteGameCoordinate coordinate,
            final PenteGameCoordinate leftTop, final PenteGameCoordinate bottomRight,
            final float[] samePieceIslandArea,
            final boolean[][] isVisited
    ) {
        assert board.get(coordinate) != null;
        isVisited[coordinate.y][coordinate.x] = true;
        samePieceIslandArea[0] += 1f;

        leftTop.y = Math.min(leftTop.y, coordinate.y);
        leftTop.x = Math.min(leftTop.x, coordinate.x);
        bottomRight.y = Math.max(bottomRight.y, coordinate.y);
        bottomRight.x = Math.max(bottomRight.x, coordinate.x);

        final PenteGamePiece current = board.get(coordinate);

        for (final int[] direction : PenteGameBoardUtil.directions) {
            int newY = coordinate.y + direction[0];
            int newX = coordinate.x + direction[1];

            DFS_helper(board, current, newY, newX, leftTop, bottomRight, samePieceIslandArea, isVisited);

            newY = coordinate.y - direction[0];
            newX = coordinate.x - direction[1];

            DFS_helper(board, current, newY, newX, leftTop, bottomRight, samePieceIslandArea, isVisited);
        }
    }

    private void DFS_helper(
            final PenteGameBoard board,
            final PenteGamePiece current,
            int newY, int newX,
            final PenteGameCoordinate leftTop, final PenteGameCoordinate bottomRight,
            final float[] samePieceIslandArea,
            final boolean[][] isVisited
    ) {
        if (!PenteGameCoordinate.isCoordinateValid(newY, newX)) {
            samePieceIslandArea[0] -= 1 / (float)8;
        } else if (!isVisited[newY][newX]){
            if (board.get(newY, newX) == current) {
                DFS(board, PenteGameCoordinate.getCoordinate(newY, newX), leftTop, bottomRight, samePieceIslandArea, isVisited);
            } else if (board.get(newY, newX) != null) {
                samePieceIslandArea[0] -= 1 / (float) 8;
            }
        }
    }

    private float captureScore(final PenteGameState state, final PenteGamePlayer player) {
        final int numOfCaptures = (player.playerType == PenteGamePiece.WHITE) ?
                state.whiteCaptures : state.blackCaptures;
        return ((float)numOfCaptures / PenteGame.NUM_OF_CAPTURES_TO_FINISH);
    }
}
