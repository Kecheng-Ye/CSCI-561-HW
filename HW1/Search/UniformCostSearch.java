package Search;

import Problem.Action;
import Problem.Problem;
import Problem.State;

import java.util.*;

class PriorityQueueWithHashSetForNode<S extends State, A extends Action> {
    PriorityQueue<Node<S, A>> pq;
    HashSet<S> hashSet;

    public PriorityQueueWithHashSetForNode(Collection<Node<S, A>> list, Comparator<Node<S, A>> comparator) {
        pq = new PriorityQueue<>(comparator);
        pq.addAll(list);
        hashSet = new HashSet<>();
        list.forEach(node -> hashSet.add(node.state));
    }

    public boolean add(Node<S, A> val) {
        pq.add(val);
        hashSet.add(val.state);
        return true;
    }

    public Node<S, A> pop() {
        if (isEmpty()) {
            return null;
        }

        Node<S, A> val = pq.poll();
        assert val != null;
        hashSet.remove(val.state);

        return val;
    }

    public boolean contains(S state) {
        return hashSet.contains(state);
    }

    public boolean isEmpty() {
        return pq.isEmpty();
    }

    public Node<S, A> getNodeByState(S state) {
        assert contains(state);

        for (Node<S, A> cur : pq) {
            if (cur.state.equals(state)) {
                return cur;
            }
        }

        return null;
    }

    public void remove(Node<S, A> val) {
        pq.remove(val);
        hashSet.remove(val.state);
    }
}

public class UniformCostSearch implements SearchSolver{
    @Override
    public <T extends Solution, S extends State, A extends Action> Result<T> solve(Problem<S, A> problem, SolutionGenerator<T, S, A> solutionGenerator) {
        Node<S, A> startNode = new Node<>(problem.initialState(), 0);
        if (problem.isGoalMeet(startNode.state)) {
            return Result.successfulResult(solutionGenerator.generate(startNode));
        }

        final PriorityQueueWithHashSetForNode<S, A> frontier = new PriorityQueueWithHashSetForNode<S, A>(List.of(startNode), Comparator.comparingInt(node -> node.pathCost));       // use path cost as the comparator of the pq
        final HashSet<S> explored = new HashSet<>();

        while (true) {
            if (frontier.isEmpty()) {
                return Result.failedResult();
            }

            Node<S, A> node = frontier.pop();
            if (problem.isGoalMeet(node.state)) {
                return Result.successfulResult(solutionGenerator.generate(node));
            }
            explored.add(node.state);

            for (A action : problem.validActions(node.state)) {
                Node<S, A> child = ChildNodeGenerator.generate(problem, node, action);

                if (!explored.contains(child.state) && !frontier.contains(child.state)) {
                    frontier.add(child);
                } else if (frontier.contains(child.state)) {
                    Node<S, A> prevNode = frontier.getNodeByState(child.state);
                    if (prevNode.pathCost > child.pathCost) {
                        frontier.remove(prevNode);
                        frontier.add(child);
                    }
                }
            }
        }
    }
}
