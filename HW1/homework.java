import Search.Result;
import Search.SearchMethod;
import SkiProblem.*;

import java.util.List;

public class homework {
    public static void main(String[] args) {
        final String INPUT_FILE_PATH = "hw1/input13.txt";
        final String OUTPUT_FILE_PATH = "output.txt";
        SkiProblemParser.SkiProblemParserResult parserResult = SkiProblemParser.parseFromText(INPUT_FILE_PATH);
        if (parserResult == null) {
            return;
        }

        SkiProblem problem = parserResult.problem;
        SearchMethod searchMethod = parserResult.searchMethod;
        List<Result<SkiSolution>> results = problem.solve(searchMethod);

        SkiProblemSolutionFileWritter.writeSolutionsToFile(OUTPUT_FILE_PATH, results);
    }
}
