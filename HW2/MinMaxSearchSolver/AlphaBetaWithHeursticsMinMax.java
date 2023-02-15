package MinMaxSearchSolver;

import Game.Action;
import Game.BiPlayerGame;
import Game.Player;
import Game.State;

public class AlphaBetaWithHeursticsMinMax<S extends State, A extends Action, P extends Player, Game extends BiPlayerGame<S, A, P>>  extends AlphaBetaMinMaxSolver<S, A, P, Game>{
    final Heurstics<S, A, P, Game> heurstics;
    final int depthLimit;

    public AlphaBetaWithHeursticsMinMax(final Heurstics<S, A, P, Game> heurstics, final int depthLimit) {
        this.heurstics = heurstics;
        this.depthLimit = depthLimit;
    }

    public A MinMaxDecision(final Game game, final S state) {
        float maxUtility = -2f;
        float alpha = -2f;
        float beta = 2f;

        A result = null;

        for (final A action : game.validActions(state)) {
            float curUtility = MinValue(game, game.result(state, action), alpha, beta, 0);
            if (curUtility > maxUtility) {
                maxUtility = curUtility;
                result = action;
            }
            System.out.printf("Action: %s = %f\n", action, curUtility);
        }

        assert result != null;
        return result;
    }

    float MinValue(final Game game, final S state, float alpha, float beta, final int depth) {
        if (game.terminalTest(state)) return game.utility(state);
        if (depth >= depthLimit) return heurstics.eval(game, state, game.MAX_PLAYER);

        float minUtility = 2f;

        for (final A action : game.validActions(state)) {
            minUtility = Math.min(minUtility, MaxValue(game, game.result(state, action), alpha, beta, depth + 1));
            if (minUtility < alpha) {
                return minUtility;
            }
            beta = Math.min(beta, minUtility);
        }

        return minUtility;
    }

    float MaxValue(final Game game, final S state, float alpha, final float beta, final int depth) {
        if (game.terminalTest(state)) return game.utility(state);
        if (depth >= depthLimit) return heurstics.eval(game, state, game.MAX_PLAYER);

        float maxUtility = -2f;

        for (final A action : game.validActions(state)) {
            maxUtility = Math.max(maxUtility, MinValue(game, game.result(state, action), alpha, beta, depth + 1));
            if (maxUtility > beta) {
                return maxUtility;
            }
            alpha = Math.max(alpha, maxUtility);
        }

        return maxUtility;
    }
}
