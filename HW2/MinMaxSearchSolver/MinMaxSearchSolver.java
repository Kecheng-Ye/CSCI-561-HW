package MinMaxSearchSolver;

import Game.*;

public class MinMaxSearchSolver<S extends State, A extends Action, P extends Player, Game extends BiPlayerGame<S, A, P>> {
    public MinMaxSearchSolver() {

    }

    public A MinMaxDecision(final Game game, final S state) {
        float maxUtility = -2f;
        A result = null;

        for (final A action : game.validActions(state)) {
            float curUtility = MinValue(game, game.result(state, action));
            if (curUtility > maxUtility) {
                maxUtility = curUtility;
                result = action;
            }
        }

        assert result != null;
        return result;
    }

    float MinValue(final Game game, final S state) {
        if (game.terminalTest(state)) return game.utility(state);

        float minUtility = 2f;

        for (final A action : game.validActions(state)) {
            minUtility = Math.min(minUtility, MaxValue(game, game.result(state, action)));
        }

        return minUtility;
    }

    float MaxValue(final Game game, final S state) {
        if (game.terminalTest(state)) return game.utility(state);

        float maxUtility = -2f;

        for (final A action : game.validActions(state)) {
            maxUtility = Math.max(maxUtility, MinValue(game, game.result(state, action)));
        }

        return maxUtility;
    }
}
