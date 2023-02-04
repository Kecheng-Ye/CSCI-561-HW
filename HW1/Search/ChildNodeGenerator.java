package Search;

import Problem.*;

public class ChildNodeGenerator {
    public static <S extends State, A extends Action> Node<S, A> generate(Problem<S, A> problem, Node<S, A> node, A action) {
        return new Node<>(
                problem.result(node.state, action),                                 // new state
                node,                                                               // parent node
                action,                                                             // action
                node.pathCost + problem.calculateCost(node.state, action)           // new cost
        );
    }

    public static <S extends State, A extends Action> Node<S, A> generate(Problem<S, A> problem, Node<S, A> node, A action, Heuristic h) {
        return new Node<>(
                problem.result(node.state, action),                                 // new state
                node,                                                               // parent node
                action,                                                             // action
                node.pathCost + problem.calculateCost(node.state, action),          // new cost
                h.calculate(node.state)                                             // heuristic score
        );
    }
}
