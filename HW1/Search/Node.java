package Search;

import Problem.Action;
import Problem.State;

public class Node<S extends State, A extends Action> {
    public final S state;
    public final Node<S, A> parent;
    public final A action;
    public final long pathCost;
    public final long heuristicScore;

    public Node(final S state, final Node<S, A> parent, final A action, final long pathCost) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
        this.heuristicScore = 0;
    }

    public Node(final S state, final Node<S, A> parent, final A action, final long pathCost, final long heuristicScore) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
        this.heuristicScore = heuristicScore;
    }

    public Node(final S state, final long pathCost) {
        this.state = state;
        this.parent = null;
        this.action = null;
        this.pathCost = pathCost;
        this.heuristicScore = 0;
    }
}
