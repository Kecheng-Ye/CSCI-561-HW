import MinMaxSearchSolver.*;
import PenteGame.*;
import Utils.Partition;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import MinMaxSearchSolver.*;

public class homework {
    public static void main(String[] args) {
        // final PenteGamePlayer AI = PenteGamePlayer.WHITE_PLAYER;
        // final PenteGamePlayer SELF = PenteGamePlayer.getOpponent(AI);
        // PenteGame game = new PenteGame(AI);
        // PenteGameState temp = game.initialState();
        // PenteGameHeurstics heurstics = new PenteGameHeurstics();
        //
        // final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer>
        // solver =
        // new AlphaBetaWithHeursticsMinMax<PenteGameState, PenteGameAction,
        // PenteGamePlayer>(heurstics, 2);
        // Scanner scanner = new Scanner(System.in);
        //
        // long start = System.currentTimeMillis();
        // System.out.println(solver.MinMaxDecisionMultiThread(game, temp, 8));
        // long finish = System.currentTimeMillis();
        // long timeElapsed = finish - start;
        // System.out.printf("Used time: timeElapsed %d", timeElapsed);

        // start = System.currentTimeMillis();
        // System.out.println(solver.MinMaxDecision(game, temp));
        // finish = System.currentTimeMillis();
        // timeElapsed = finish - start;
        // System.out.printf("Used time: timeElapsed %d", timeElapsed);

        // for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator();
        // it.hasNext(); ) {
        // PenteGameCoordinate coordinate = it.next();
        // System.out.println(coordinate);
        // }

        // PenteGameBoard board1 = new PenteGameBoard();
        // PenteGamePiece[][] board2 = new
        // PenteGamePiece[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
        //

        // Random random = new Random();
        // PenteGameBoard[] boards = new PenteGameBoard[100000];
        //
        // IntStream.range(0, 100000).forEach((idx) -> {
        // PenteGameBoard board = new PenteGameBoard();
        // for (int i = 0; i < 100; i++) {
        // int random1 = random.nextInt(PenteGame.BOARD_HEIGHT);
        // int random2 = random.nextInt(PenteGame.BOARD_WIDTH);
        // PenteGamePiece piece = ((random.nextInt(2) % 2) == 0) ? PenteGamePiece.WHITE
        // : PenteGamePiece.BLACK;
        // piece = ((random.nextInt(2) % 2) == 0) ? piece : null;
        // board.put(random1, random2, piece);
        //
        // final PenteGameCoordinate[] AABB = PenteGameBoardUtil.getBoardAABB(board);
        // assert AABB[0].equals(board.leftTop) : String.format("AABB: %s, self: %s",
        // AABB[0], board.leftTop);
        // assert AABB[1].equals(board.rightBottom) : board;
        // }
        // // boards[idx] = board;
        // });
        //
        // PenteGamePiece[][][] boards2 = new
        // PenteGamePiece[100000][PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
        // int count = 0;
        // for (final PenteGameBoard board : boards) {
        // boards2[count++] = board.toArrRepr();
        // }
        //
        // long start = System.currentTimeMillis();
        // int cnt = 0;
        // for (final PenteGameBoard board : boards) {
        // int a = board.hashCode();
        // boolean b = board.equals(boards[0]);
        //
        // cnt += (a + ((b) ? 1 : 0));
        // }
        // long finish = System.currentTimeMillis();
        // long timeElapsed = finish - start;
        // System.out.printf("Used time: timeElapsed %d %d", timeElapsed, cnt);
        //
        // start = System.currentTimeMillis();
        // cnt = 0;
        // for (final PenteGamePiece[][] board : boards2) {
        // int a = Arrays.deepHashCode(board);
        // boolean b = Arrays.deepEquals(board, boards2[0]);
        //
        // cnt += (a + ((b) ? 1 : 0));
        // }
        // finish = System.currentTimeMillis();
        // timeElapsed = finish - start;
        // System.out.printf("Used time: timeElapsed %d %d", timeElapsed, cnt);

        final PenteGamePlayer AI = PenteGamePlayer.BLACK_PLAYER;
        final PenteGamePlayer SELF = PenteGamePlayer.getOpponent(AI);
        PenteGame game = new PenteGame(AI);
        PenteGameState temp = game.initialState();
        PenteGameHeurstics heurstics = new PenteGameHeurstics();
        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer> solver = new AlphaBetaWithHeursticsMinMax<PenteGameState, PenteGameAction, PenteGamePlayer>(
                heurstics, 2);

        temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "10J"));
        temp = game.result(temp, PenteGameAction.generateFromStr(AI, "9J"));

        temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "10K"));
        temp = game.result(temp, PenteGameAction.generateFromStr(AI, "10I"));

        temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "8K"));
        temp = game.result(temp, PenteGameAction.generateFromStr(AI, "11H"));

        temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "9K"));
        // temp = game.result(temp, PenteGameAction.generateFromStr(AI, "10F"));
        //
        // temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "12I"));
        // temp = game.result(temp, PenteGameAction.generateFromStr(AI, "13J"));
        //
        // temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "13I"));
        // temp = game.result(temp, PenteGameAction.generateFromStr(AI, "14I"));
        //
        // temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "12K"));
        // temp = game.result(temp, PenteGameAction.generateFromStr(AI, "11I"));
        //
        // temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "12I"));

        System.out.println(temp);
        // System.out.println(heurstics.eval(game.result(question,
        // PenteGameAction.generateFromStr(AI, "9F")), AI));
        // System.out.println(heurstics.eval(game.result(question,
        // PenteGameAction.generateFromStr(AI, "7H")), AI));
        MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer>.MinMaxDecisionResult result = solver
                .MinMaxDecisionMultiThread(game, temp, 8);
        System.out.println(result.utility);
        temp = game.result(temp, result.action);
        System.out.println(temp);

        temp = game.result(temp, PenteGameAction.generateFromStr(SELF, "11K"));
        System.out.println(temp);
        System.out.println(heurstics.eval(temp, AI));
        // IntStream.range(1, 10).forEach((idx) -> {
        // System.out.println(game.result(question,
        // solver.MinMaxDecisionMultiThread(game, question, 8).action));
        // });
        // // System.out.println(temp);
        // // for (final PenteGameAction action : game.validActions(temp)) {
        // // temp = game.result(temp, action);
        // // }
        // // System.out.println(temp);
        //
        // System.out.println(temp);
        // PenteGameState finalTemp = temp;
        // IntStream.range(1, 5).forEach((idx) -> {
        // long start = System.currentTimeMillis();
        // // action = solver.MinMaxDecisionMultiThread(game, temp, 5).action;
        // final PenteGameAction action = solver.MinMaxDecisionMultiThread(game,
        // finalTemp, 8).action;
        // long finish = System.currentTimeMillis();
        // long timeElapsed = finish - start;
        // System.out.printf("Used time: timeElapsed %d\n", timeElapsed);
        // System.out.println(game.result(finalTemp, action));
        // });
    }
}
