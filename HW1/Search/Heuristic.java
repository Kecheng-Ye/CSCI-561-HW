package Search;

import Problem.State;

public interface Heuristic {
    long calculate(State state);
}
