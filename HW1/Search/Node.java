package Search;

import Problem.Action;
import Problem.State;

public class Node<S extends State, A extends Action> {
    public final S state;
    public final Node<S, A> parent;
    public final A action;
    public final int pathCost;


    public Node(final S state, final Node<S, A> parent, final A action, final int pathCost) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.pathCost = pathCost;
    }

    public Node(final S state, final int pathCost) {
        this.state = state;
        this.parent = null;
        this.action = null;
        this.pathCost = pathCost;
    }

//    @Override
//    public int hashCode() {
//        return state.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (! (obj instanceof Node)) {
//            return false;
//        }
//        return state.equals(((Node<?, ?>) obj).state);
//    }
}
