package MinMaxSearchSolver;

import Game.*;

import java.util.List;

public class AlphaBetaMinMaxSolver<S extends State, A extends Action, P extends Player> extends MinMaxSearchSolver<S, A, P>{
    public AlphaBetaMinMaxSolver() {

    }

    @Override
    MinMaxDecisionResult MinMaxDecision(final BiPlayerGame<S, A, P> game, final S state, final List<A> actions) {
        float maxUtility = -2f;
        float alpha = -2f;
        float beta = 2f;

        A result = null;

        for (final A action : actions) {
            float curUtility = MinValue(game, game.result(state, action), alpha, beta);
            if (!isUtilityValid(curUtility)) continue;
            if (curUtility > maxUtility) {
                maxUtility = curUtility;
                result = action;
            }
            System.out.println(maxUtility);
        }

        return new MinMaxDecisionResult(result, maxUtility);
    }

    float MinValue(final BiPlayerGame<S, A, P> game, final S state, float alpha, float beta) {
        if (visited.containsKey(state)) {
            return visited.get(state);
        }

        final BiPlayerGameTerminationStatus<S, A, P> terminationStatus = game.terminalTest(state);
        if (terminationStatus.isFinished) return returnUtilityWithMemo(state, terminationStatus.utility(game.MAX_PLAYER));

        float minUtility = 2f;

        for (final A action : game.validActions(state)) {
            final float curUtility = MaxValue(game, game.result(state, action), alpha, beta);
            if (!isUtilityValid(curUtility)) continue;

            minUtility = Math.min(minUtility, curUtility);
            if (minUtility < alpha) {
                return returnUtilityWithMemo(state, minUtility);
            }
            beta = Math.min(beta, minUtility);
        }

        return returnUtilityWithMemo(state, minUtility);
    }

    float MaxValue(final BiPlayerGame<S, A, P> game, final S state, float alpha, final float beta) {
        if (visited.containsKey(state)) {
            return visited.get(state);
        }

        final BiPlayerGameTerminationStatus<S, A, P> terminationStatus = game.terminalTest(state);
        if (terminationStatus.isFinished) return returnUtilityWithMemo(state, terminationStatus.utility(game.MAX_PLAYER));

        float maxUtility = -2f;

        for (final A action : game.validActions(state)) {
            final float curUtility = MinValue(game, game.result(state, action), alpha, beta);
            if (!isUtilityValid(curUtility)) continue;

            maxUtility = Math.max(maxUtility, curUtility);
            if (maxUtility > beta) {
                return returnUtilityWithMemo(state, maxUtility);
            }
            alpha = Math.max(alpha, maxUtility);
        }

        return returnUtilityWithMemo(state, maxUtility);
    }
}
