package Search;

import Problem.Action;
import Problem.Problem;
import Problem.State;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class AStarSearch implements SearchSolver{
    private Heuristic heuristic;

    public AStarSearch() {
        this.heuristic = null;
    }

    public AStarSearch(final Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public void setHeuristic(final Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public <T extends Solution, S extends State, A extends Action> Result<T> solve(Problem<S, A> problem, SolutionGenerator<T, S, A> solutionGenerator) {
        assert heuristic != null;

        Node<S, A> startNode = new Node<>(problem.initialState(), 0);
        if (problem.isGoalMeet(startNode.state)) {
            return Result.successfulResult(solutionGenerator.generate(startNode));
        }

        // use path cost + heuristic cost as the comparator of the pq
        final PriorityQueueWithHashSetForNode<S, A> frontier = new PriorityQueueWithHashSetForNode<S, A>(List.of(startNode), Comparator.comparingLong(node -> node.pathCost + node.heuristicScore));
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
                Node<S, A> child = ChildNodeGenerator.generate(problem, node, action, heuristic);

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
