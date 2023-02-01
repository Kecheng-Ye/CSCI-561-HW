package Search;

import Problem.*;
import java.util.*;

class QueueWithHashSetForNode<S extends State, A extends Action>{
    Queue<Node<S, A>> queue;
    HashSet<S> hashSet;

    public QueueWithHashSetForNode(Collection<Node<S, A>> list) {
        queue = new LinkedList<>(list);
        hashSet = new HashSet<>();
        list.forEach(node -> hashSet.add(node.state));
    }

    public boolean add(Node<S, A> val) {
        queue.add(val);
        hashSet.add(val.state);
        return true;
    }

    public Node<S, A> pop() {
        Node<S, A> val = queue.poll();
        assert val != null;
        hashSet.remove(val.state);

        return val;
    }

    public boolean contains(S state) {
        return hashSet.contains(state);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

public class BFS implements SearchSolver {
    public<T extends Solution, S extends State, A extends Action> Result<T> solve(final Problem<S, A> problem, final SolutionGenerator<T, S, A> solutionGenerator) {
        Node<S, A> startNode = new Node<>(problem.initialState(), 0);
        if (problem.isGoalMeet(startNode.state)) {
            return Result.successfulResult(solutionGenerator.generate(startNode));
        }

        QueueWithHashSetForNode<S, A> frontier = new QueueWithHashSetForNode<S, A>(List.of(startNode));
        HashSet<S> explored = new HashSet<>();

        while(true) {
            if (frontier.isEmpty()) {
                return Result.failedResult();
            }

            Node<S, A> node = frontier.pop();
            explored.add(node.state);

            for (A action : problem.validActions(node.state)) {
                Node<S, A> child = ChildNodeGenerator.generate(problem, node, action);

                if (!explored.contains(child.state) && !frontier.contains(child.state)) {
                    if (problem.isGoalMeet(child.state)) {
                        return Result.successfulResult(solutionGenerator.generate(child));
                    }

                    frontier.add(child);
                }
            }
        }
    }
}
