package Search;

import Problem.Action;
import Problem.State;

public interface SolutionGenerator<T extends Solution, S extends State, A extends Action> {
    T generate(Node<S, A> node);
}
