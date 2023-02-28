package MinMaxSearchSolver;

import Game.*;

import java.util.List;

public class AlphaBetaWithHeursticsMinMax<S extends State, A extends Action, P extends Player>  extends AlphaBetaMinMaxSolver<S, A, P>{
    final Heurstics<S, P> heurstics;
    final int depthLimit;

    public AlphaBetaWithHeursticsMinMax(final Heurstics<S, P> heurstics, final int depthLimit) {
        this.heurstics = heurstics;
        this.depthLimit = depthLimit;
    }

    @Override
    MinMaxDecisionResult MinMaxDecision(final BiPlayerGame<S, A, P> game, final S state, final List<A> actions) {
        float maxUtility = -2f;
        float alpha = -2f;
        float beta = 2f;

        A result = null;

        for (final A action : actions) {
            float curUtility = MinValue(game, game.result(state, action), alpha, beta, 0);
            if (!isUtilityValid(curUtility)) continue;
            if (curUtility > maxUtility) {
                maxUtility = curUtility;
                result = action;
            }
        }

        return new MinMaxDecisionResult(result, maxUtility);
    }

    float MinValue(final BiPlayerGame<S, A, P> game, final S state, float alpha, float beta, final int depth) {
        if (visited.containsKey(state)) {
            return visited.get(state);
        }

        final BiPlayerGameTerminationStatus<S, A, P> terminationStatus = game.terminalTest(state);
        if (terminationStatus.isFinished) return terminationStatus.utility(game.MAX_PLAYER);
        if (depth >= depthLimit) return heurstics.eval(state, game.MAX_PLAYER);

        float minUtility = 2f;

        for (final A action : game.validActions(state)) {
            final float curUtility = MaxValue(game, game.result(state, action), alpha, beta, depth + 1);
            if (!isUtilityValid(curUtility)) continue;

            minUtility = Math.min(minUtility, curUtility);
            if (minUtility < alpha) {
                visited.put(state, minUtility);
                return minUtility;
            }
            beta = Math.min(beta, minUtility);
        }

        visited.put(state, minUtility);
        return minUtility;
    }

    float MaxValue(final BiPlayerGame<S, A, P> game, final S state, float alpha, final float beta, final int depth) {
        if (visited.containsKey(state)) {
            return visited.get(state);
        }

        final BiPlayerGameTerminationStatus<S, A, P> terminationStatus = game.terminalTest(state);
        if (terminationStatus.isFinished) return terminationStatus.utility(game.MAX_PLAYER);
        if (depth >= depthLimit) return heurstics.eval(state, game.MAX_PLAYER);

        float maxUtility = -2f;

        for (final A action : game.validActions(state)) {
            final float curUtility = MinValue(game, game.result(state, action), alpha, beta, depth + 1);
            if (!isUtilityValid(curUtility)) continue;

            maxUtility = Math.max(maxUtility, MinValue(game, game.result(state, action), alpha, beta, depth + 1));
            if (maxUtility > beta) {
                visited.put(state, maxUtility);
                return maxUtility;
            }
            alpha = Math.max(alpha, maxUtility);
        }

        visited.put(state, maxUtility);
        return maxUtility;
    }
}
