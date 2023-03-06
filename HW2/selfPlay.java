import MinMaxSearchSolver.MinMaxSearchSolver;
import PenteGame.*;

import java.util.List;
import java.util.Scanner;
import MinMaxSearchSolver.*;

public class selfPlay {
    public static void main(String[] args) {
        final PenteGamePlayer AI = PenteGamePlayer.BLACK_PLAYER;
        final PenteGamePlayer SELF = PenteGamePlayer.getOpponent(AI);
        PenteGame game = new PenteGame(AI);
        PenteGameState temp = game.initialState();
        PenteGameHeurstics heurstics = new PenteGameHeurstics();
//        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame> solver =
//                new MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame>();

//        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame> solver =
//                new AlphaBetaMinMaxSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame>();

        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer> solver =
                new AlphaBetaWithHeursticsMinMax<PenteGameState, PenteGameAction, PenteGamePlayer>(heurstics, 2);
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
                System.out.printf("Used time: timeElapsed %d\n", timeElapsed);
            } else {
                do {
                    System.out.print("Enter a Action: ");
                    action = PenteGameAction.parseFromStr(SELF, scanner.nextLine());
                } while (temp.board.get(action.coordinate) != null);
            }

            temp = game.result(temp, action);
        }

        System.out.println(temp);
    }
}
