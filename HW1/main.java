import SkiProblem.SkiProblem;
import SkiProblem.Coordinate;
import SkiProblem.SearchMethod;

import java.util.*;

public class main {
    public static void main(String[] args) {
        List<List<Integer>> map = new ArrayList<>();
        map.add(Arrays.asList(-10, 40, 34, 21, 42, 37, 18, 7));
        map.add(Arrays.asList(-20, 10, 5, 27, -6, 5, 2, 0));
        map.add(Arrays.asList(-30, 8, 17, -3, -4, -1, 0, 4));
        map.add(Arrays.asList(-25, -4, 12, 14, -1, 9, 6, 9));
        map.add(Arrays.asList(-15, -9, 46, 6, 25, 11, 31, -21));
        map.add(Arrays.asList(-5, -6,-3, -7, 0, 25, 53, -42));
        SkiProblem problem = new SkiProblem(8, 6, map, new Coordinate(4, 4), 0, List.of(new Coordinate(2, 1), new Coordinate(6, 3)));
        System.out.println(problem.solve(SearchMethod.BFS));
    }
}
