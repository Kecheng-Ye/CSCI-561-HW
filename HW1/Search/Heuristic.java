package Search;

import Problem.State;

public interface Heuristic {
    int calculate(State state);
}
