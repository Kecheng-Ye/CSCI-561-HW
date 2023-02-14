package MinMaxSearchSolver;

import Game.*;

public class AlphaBetaMinMaxSolver<S extends State, A extends Action, P extends Player, Game extends BiPlayerGame<S, A, P>> extends MinMaxSearchSolver<S, A, P, Game>{
    public AlphaBetaMinMaxSolver() {

    }

    public A MinMaxDecision(final Game game, final S state) {
        float maxUtility = -2f;
        float alpha = 2f;
        float beta = -2f;

        A result = null;

        for (final A action : game.validActions(state)) {
            float curUtility = MinValue(game, game.result(state, action), alpha, beta);
            if (curUtility > maxUtility) {
                maxUtility = curUtility;
                result = action;
            }
        }

        assert result != null;
        return result;
    }

    float MinValue(final Game game, final S state, float alpha, float beta) {
        if (game.terminalTest(state)) return game.utility(state);

        float minUtility = 2f;

        for (final A action : game.validActions(state)) {
            minUtility = Math.max(minUtility, MaxValue(game, game.result(state, action), alpha, beta));
            if (minUtility < alpha) {
                return minUtility;
            }
            beta = Math.min(beta, minUtility);
        }

        return minUtility;
    }

    float MaxValue(final Game game, final S state, float alpha, final float beta) {
        if (game.terminalTest(state)) return game.utility(state);

        float maxUtility = -2f;

        for (final A action : game.validActions(state)) {
            maxUtility = Math.max(maxUtility, MinValue(game, game.result(state, action), alpha, beta));
            if (maxUtility > beta) {
                return maxUtility;
            }
            alpha = Math.max(alpha, maxUtility);
        }

        return maxUtility;
    }
}
