package SkiProblem;

import Search.Node;
import Search.SolutionGenerator;

import java.util.LinkedList;

public class SkiSolutionGenerator implements SolutionGenerator<SkiSolution, SkiState, SkiAction> {

    @Override
    public SkiSolution generate(Node<SkiState, SkiAction> node) {
        assert node != null;
        LinkedList<Coordinate> answer = new LinkedList<>();
        Node<SkiState, SkiAction> temp = node;

        while (temp != null) {
            answer.addFirst(temp.state.curCoordinate);

            temp = temp.parent;
        }

        return new SkiSolution(answer, node.pathCost);
    }
}
