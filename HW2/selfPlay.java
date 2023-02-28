import MinMaxSearchSolver.MinMaxSearchSolver;
import PenteGame.*;

import java.util.Scanner;
import MinMaxSearchSolver.*;

public class selfPlay {
    public static void main(String[] args) {
        final PenteGamePlayer AI = PenteGamePlayer.WHITE_PLAYER;
        final PenteGamePlayer SELF = PenteGamePlayer.getOpponent(AI);
        PenteGame game = new PenteGame(AI);
        PenteGameState temp = game.initialState();
        PenteGameHeurstics heurstics = new PenteGameHeurstics();
//        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame> solver =
//                new MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame>();

//        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame> solver =
//                new AlphaBetaMinMaxSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame>();

        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer> solver =
                new AlphaBetaMinMaxSolver<PenteGameState, PenteGameAction, PenteGamePlayer>();
        Scanner scanner = new Scanner(System.in);

        while (!(game.terminalTest(temp)).isFinished) {
            System.out.println(temp);
            final PenteGamePlayer currentPlayer = game.playerForNextMove(temp);
            PenteGameAction action = null;
            if (currentPlayer == AI) {
                long start = System.currentTimeMillis();
                action = solver.MinMaxDecision(game, temp).action;
                long finish = System.currentTimeMillis();
                long timeElapsed = finish - start;
                System.out.printf("Used time: timeElapsed %d", timeElapsed);
            } else {
                System.out.print("Enter a Action: ");
                action = PenteGameAction.generateFromStr(SELF, scanner.nextLine());
            }

            temp = game.result(temp, action);
        }
    }
}
