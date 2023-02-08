package Problem;

import java.util.List;

public interface Problem<S extends State, A extends Action> {
    public S initialState();

    // Transition model
    public S result(S state, A action);

    // Goal test
    public boolean isGoalMeet(S state);

    // Generate valid actions based on current state
    public List<A> validActions(S state);

    // Path cost
    public long calculateCost(S state, A action);
}
