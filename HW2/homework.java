import MinMaxSearchSolver.*;
import PenteGame.*;
import Utils.Partition;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.IntStream;

public class homework {
    public static void main(String[] args) {
        final PenteGamePlayer AI = PenteGamePlayer.WHITE_PLAYER;
        final PenteGamePlayer SELF = PenteGamePlayer.getOpponent(AI);
        PenteGame game = new PenteGame(AI);
        PenteGameState temp = game.initialState();
        PenteGameHeurstics heurstics = new PenteGameHeurstics();

        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer> solver =
                new AlphaBetaWithHeursticsMinMax<PenteGameState, PenteGameAction, PenteGamePlayer>(heurstics, 5);
        Scanner scanner = new Scanner(System.in);

        long start = System.currentTimeMillis();
        System.out.println(solver.MinMaxDecisionMultiThread(game, temp, 5));
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.printf("Used time: timeElapsed %d", timeElapsed);


//        start = System.currentTimeMillis();
//        System.out.println(solver.MinMaxDecision(game, temp));
//        finish = System.currentTimeMillis();
//        timeElapsed = finish - start;
//        System.out.printf("Used time: timeElapsed %d", timeElapsed);

//        for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator(); it.hasNext(); ) {
//            PenteGameCoordinate coordinate = it.next();
//            System.out.println(coordinate);
//        }

//        PenteGameBoard board1 = new PenteGameBoard();
//        PenteGamePiece[][] board2 = new PenteGamePiece[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
//

//        Random random = new Random();
//        PenteGameBoard[] boards = new PenteGameBoard[100000];
//
//        IntStream.range(0, 100000).forEach((idx) -> {
//            PenteGameBoard board = new PenteGameBoard();
//            for (int i = 0; i < 100; i++) {
//                int random1 = random.nextInt(PenteGame.BOARD_HEIGHT);
//                int random2 = random.nextInt(PenteGame.BOARD_WIDTH);
//                PenteGamePiece piece = ((random.nextInt(2) % 2) == 0) ? PenteGamePiece.WHITE : PenteGamePiece.BLACK;
//                piece = ((random.nextInt(2) % 2) == 0) ? piece : null;
//                board.put(random1, random2, piece);
//            }
//            boards[idx] = board;
//        });
//
//        PenteGamePiece[][][] boards2 = new PenteGamePiece[100000][PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
//        int count = 0;
//        for (final PenteGameBoard board : boards) {
//            boards2[count++] = board.toArrRepr();
//        }
//
//        long start = System.currentTimeMillis();
//        int cnt = 0;
//        for (final PenteGameBoard board : boards) {
//            int a = board.hashCode();
//            boolean b = board.equals(boards[0]);
//
//            cnt += (a + ((b) ? 1 : 0));
//        }
//        long finish = System.currentTimeMillis();
//        long timeElapsed = finish - start;
//        System.out.printf("Used time: timeElapsed %d %d", timeElapsed, cnt);
//
//        start = System.currentTimeMillis();
//        cnt = 0;
//        for (final PenteGamePiece[][] board : boards2) {
//            int a = Arrays.deepHashCode(board);
//            boolean b = Arrays.deepEquals(board, boards2[0]);
//
//            cnt += (a + ((b) ? 1 : 0));
//        }
//        finish = System.currentTimeMillis();
//        timeElapsed = finish - start;
//        System.out.printf("Used time: timeElapsed %d %d", timeElapsed, cnt);
    }
}
