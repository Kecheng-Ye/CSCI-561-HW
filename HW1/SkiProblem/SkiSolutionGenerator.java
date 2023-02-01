package SkiProblem;

import Search.Node;
import Search.SolutionGenerator;

import java.util.LinkedList;

public class SkiSolutionGenerator implements SolutionGenerator<SkiSolution, SkiState, SkiAction> {

    @Override
    public SkiSolution generate(Node<SkiState, SkiAction> node) {
        LinkedList<Coordinate> answer = new LinkedList<>();
        Node<SkiState, SkiAction> temp = node;

        while (temp != null) {
            answer.addFirst(temp.state.coordinate);

            temp = temp.parent;
        }

        return new SkiSolution(answer);
    }
}
