import MinMaxSearchSolver.MinMaxSearchSolver;
import PenteGame.*;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import MinMaxSearchSolver.*;

public class homework {
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

        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame> solver =
                new AlphaBetaWithHeursticsMinMax<PenteGameState, PenteGameAction, PenteGamePlayer, PenteGame>(heurstics, 3);
        Scanner scanner = new Scanner(System.in);

        while (!game.terminalTest(temp)) {
            System.out.println(temp);
//            System.out.printf("Heurstics: %f\n", heurstics.eval(game, temp));
            final PenteGamePlayer currentPlayer = game.playerForNextMove(temp);
            PenteGameAction action = null;
            if (currentPlayer == AI) {
                action = solver.MinMaxDecision(game, temp);
            } else {
                System.out.print("Enter a Action: ");
                action = PenteGameAction.generateFromStr(SELF, scanner.nextLine());
            }

            temp = game.result(temp, action);
        }

        System.out.println(temp);
        System.out.println(game.utility(temp));

//        for (final PenteGameCoordinate coordinate : game) {
//            System.out.println(coordinate);
//        }
//
//        for (final PenteGameCoordinate coordinate : game) {
//            System.out.println(coordinate);
//        }
    }
}
