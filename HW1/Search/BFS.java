package Search;

import Problem.*;
import java.util.*;

class QueueWithHashSet<T>{
    Queue<T> queue;
    HashSet<T> hashSet;

    public QueueWithHashSet(Collection<T> list) {
        queue = new LinkedList<>(list);
        hashSet = new HashSet<>(list);
    }

    public boolean add(T val) {
        queue.add(val);
        hashSet.add(val);
        return true;
    }

    public T pop() {
        T val = queue.poll();
        hashSet.remove(val);

        return val;
    }

    public boolean contains(T val) {
        return hashSet.contains(val);
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

        QueueWithHashSet<Node<S, A>> frontier = new QueueWithHashSet<>(List.of(startNode));
        HashSet<Node<S, A>> explored = new HashSet<>();

        while(true) {
            if (frontier.isEmpty()) {
                return Result.failedResult();
            }

            Node<S, A> node = frontier.pop();
            explored.add(node);

            for (A action : problem.validActions(node.state)) {
                Node<S, A> child = ChildNodeGenerator.generate(problem, node, action);
//                System.out.println(child.state);
//                System.out.println(explored);
//                System.out.println(explored.contains(child));
//                System.out.println(frontier.contains(child));

                if (!explored.contains(child) && !frontier.contains(child)) {
                    if (problem.isGoalMeet(child.state)) {
                        return Result.successfulResult(solutionGenerator.generate(child));
                    }

                    frontier.add(child);
                }
            }
        }
    }
}
