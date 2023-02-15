package PenteGame;

import MinMaxSearchSolver.Heurstics;

import java.util.Arrays;

public class PenteGameHeurstics implements Heurstics<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame> {
    @Override
    public float eval(final PenteGame game, final PenteGameState state, PenteGamePlayer self) {
        PenteGamePlayer opponent = PenteGamePlayer.getOpponent(self);

        final float captureScore = captureScore(state, self) - captureScore(state, opponent);
        final float connectedComponentScore = connectedComponentScore(game, state, self) - connectedComponentScore(game, state, opponent);

        return captureScore * 0.5f + connectedComponentScore * 0.5f;
    }

    private PenteGameCoordinate leftTop = null;
    private PenteGameCoordinate bottomRight = null;
    private float samePieceIslandArea = 0f;
    private final boolean[][] isVisited = new boolean[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];

    private float connectedComponentScore(final PenteGame game, final PenteGameState state, final PenteGamePlayer player) {
        for (boolean[] row: isVisited)
            Arrays.fill(row, false);

        float totalSamePieceIslandArea = 0f;
        float totalRectangluar = 0f;

        for (final PenteGameCoordinate coordinate : game) {
            final int y = coordinate.y;
            final int x = coordinate.x;

            if (isVisited[y][x] || state.board[y][x] != player.playerType) continue;

            leftTop = coordinate;
            bottomRight = coordinate;
            samePieceIslandArea = 0f;
            DFS(state.board, coordinate);

            totalSamePieceIslandArea += samePieceIslandArea;
            totalRectangluar += (bottomRight.y - leftTop.y + 1) * (bottomRight.x - leftTop.x + 1);
        }

        if (totalRectangluar == 0f) return 0;

        return Math.min(1.0f, (totalSamePieceIslandArea * 1.5f / totalRectangluar) * 2 * (totalRectangluar / PenteGame.MAX_NUM_PIECES_IN_A_ROW));
    }

    private void DFS(final PenteGamePiece[][] board, final PenteGameCoordinate coordinate) {
        isVisited[coordinate.y][coordinate.x] = true;
        samePieceIslandArea += 1f;
        leftTop = PenteGameCoordinate.getCoordinate(
                Math.min(leftTop.y, coordinate.y),
                Math.min(leftTop.x, coordinate.x)
        );
        bottomRight = PenteGameCoordinate.getCoordinate(
                Math.max(bottomRight.x, coordinate.x),
                Math.max(bottomRight.y, coordinate.y)
        );

        final PenteGamePiece current = board[coordinate.y][coordinate.x];

        for (final int[] direction : PenteGameBoardUtil.directions) {
            int newY = coordinate.y + direction[0];
            int newX = coordinate.x + direction[1];

            DFS_helper(board, current, newY, newX);

            newY = coordinate.y - direction[0];
            newX = coordinate.x - direction[1];

            DFS_helper(board, current, newY, newX);
        }
    }

    private void DFS_helper(PenteGamePiece[][] board, PenteGamePiece current, int newY, int newX) {
        if (!PenteGameCoordinate.isCoordinateValid(newY, newX)) {
            samePieceIslandArea -= 1 / (float)8;
        } else if (!isVisited[newY][newX]){
            if (board[newY][newX] == current) {
                DFS(board, PenteGameCoordinate.getCoordinate(newY, newX));
            } else if (board[newY][newX] != null) {
                samePieceIslandArea -= 1 / (float) 8;
            }
        }
    }

    private float captureScore(final PenteGameState state, final PenteGamePlayer player) {
        final int numOfCaptures = (player.playerType == PenteGamePiece.WHITE) ?
                state.whiteCaptures : state.blackCaptures;
        return ((float)numOfCaptures / PenteGame.NUM_OF_CAPTURES_TO_FINISH);
    }
}
