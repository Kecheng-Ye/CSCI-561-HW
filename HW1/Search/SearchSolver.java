package Search;

import Problem.*;

public interface SearchSolver {
    public<T extends Solution, S extends State, A extends Action> Result<T> solve(final Problem<S, A> problem, final SolutionGenerator<T, S, A> solutionGenerator);
}
