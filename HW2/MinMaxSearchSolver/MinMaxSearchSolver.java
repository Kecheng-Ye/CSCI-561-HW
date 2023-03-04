package MinMaxSearchSolver;

import Game.*;
import Utils.Partition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MinMaxSearchSolver<S extends State, A extends Action, P extends Player> {
    List<MinMaxDecisionResult> multiThreadResults;
    ConcurrentHashMap<S, Float> visited;

    public class MinMaxDecisionResult {
        public final A action;
        public final float utility;

        public MinMaxDecisionResult(A action, float utility) {
            this.action = action;
            this.utility = utility;
        }

        @Override
        public String toString() {
            return String.format("MinMaxDecisionResult:{Action: %s, Utility: %f}", action, utility);
        }
    }

    public MinMaxSearchSolver() {
        this.multiThreadResults = new ArrayList<>();
        this.visited = new ConcurrentHashMap<>();
    }

    public MinMaxDecisionResult MinMaxDecision(final BiPlayerGame<S, A, P> game, final S state) {
        this.visited.clear();
        final MinMaxDecisionResult answer = MinMaxDecision(game, state, game.validActions(state));
        assert answer.action != null;

        return answer;
    }

    MinMaxDecisionResult MinMaxDecision(final BiPlayerGame<S, A, P> game, final S state, final List<A> actions) {
        float maxUtility = -2f;
        A result = null;

        for (final A action : actions) {
            float curUtility = MinValue(game, game.result(state, action));
            if (!isUtilityValid(curUtility)) continue;
            if (curUtility > maxUtility) {
                maxUtility = curUtility;
                result = action;
            }
        }

        assert result != null;
        return new MinMaxDecisionResult(result, maxUtility);
    }

    float MinValue(final BiPlayerGame<S, A, P> game, final S state) {
        if (visited.containsKey(state)) {
            return visited.get(state);
        }

        final BiPlayerGameTerminationStatus<S, A, P> terminationStatus = game.terminalTest(state);
        if (terminationStatus.isFinished) return terminationStatus.utility(game.MAX_PLAYER);

        float minUtility = 2f;

        for (final A action : game.validActions(state)) {
            final float curUtility = MaxValue(game, game.result(state, action));
            if (!isUtilityValid(curUtility)) continue;

            minUtility = Math.min(minUtility, curUtility);
        }

        visited.put(state, minUtility);
        return minUtility;
    }

    float MaxValue(final BiPlayerGame<S, A, P> game, final S state) {
        if (visited.containsKey(state)) {
            return visited.get(state);
        }

        final BiPlayerGameTerminationStatus<S, A, P> terminationStatus = game.terminalTest(state);
        if (terminationStatus.isFinished) return terminationStatus.utility(game.MAX_PLAYER);

        float maxUtility = -2f;

        for (final A action : game.validActions(state)) {
            final float curUtility = MinValue(game, game.result(state, action));
            if (!isUtilityValid(curUtility)) continue;

            maxUtility = Math.max(maxUtility, curUtility);
        }

        visited.put(state, maxUtility);
        return maxUtility;
    }

    public MinMaxDecisionResult MinMaxDecisionMultiThread(final BiPlayerGame<S, A, P> game, final S state, int numOfThread) {
        this.visited.clear();
        multiThreadResults = new ArrayList<>(Collections.nCopies(numOfThread, null));
        final List<A> validActions = game.validActions(state);
        final List<List<A>> actionList = Partition.partition(validActions, numOfThread);
        Thread[] threads = new Thread[numOfThread];

        for (int i = 0; i < numOfThread; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                final MinMaxDecisionResult result = MinMaxDecision(game, state, actionList.get(finalI));
                multiThreadResults.set(finalI, result);
            });
            thread.start();
            threads[i] = thread;
        }

        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }

        final MinMaxDecisionResult maxDecisionResult = multiThreadResults
                .stream()
                .reduce(multiThreadResults.get(0),
                        (prevMaxAction, curr) -> (curr.utility > prevMaxAction.utility) ? curr : prevMaxAction
                );

        if (maxDecisionResult.action == null) {
            return new MinMaxDecisionResult(validActions.get(0), -2f);
        }

        return maxDecisionResult;
    }

    protected boolean isUtilityValid(float utility) {
        return ((utility >= -1f) && (utility <= 1f));
    }
}
