import MinMaxSearchSolver.*;
import PenteGame.*;

public class homework {
    public static void main(String[] args) {
        final String INPUT_PATH = "input.txt";
        final String OUTPUT_PATH = "output.txt";

        final PenteGameParser.PenteGameParserResult parserResult = PenteGameParser.parseFromFile(INPUT_PATH);
        assert parserResult != null;
        final PenteGame game = parserResult.game;
        final PenteGameState state = parserResult.state;

        final PenteGameHeurstics heurstics = new PenteGameHeurstics();
        final MinMaxSearchSolver<PenteGameState, PenteGameAction, PenteGamePlayer> solver =
                new AlphaBetaWithHeursticsMinMax<PenteGameState, PenteGameAction, PenteGamePlayer>(heurstics, 2);

        final PenteGameAction action = solver.MinMaxDecision(game, state).action;
        action.dumpIntoFile(OUTPUT_PATH);
    }
}
