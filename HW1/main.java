import Search.SearchMethod;
import SkiProblem.SkiProblem;
import SkiProblem.SkiProblemParser;

public class main {
    public static void main(String[] args) {
        final String INPUT_FILE_PATH = "UCS_Test_Input.txt";
        SkiProblemParser.SkiProblemParserResult parserResult = SkiProblemParser.parseFromText(INPUT_FILE_PATH);
        if (parserResult == null) {
            return;
        }

        SkiProblem problem = parserResult.problem;
        SearchMethod searchMethod = parserResult.searchMethod;
        System.out.println(problem.solve(searchMethod));
    }
}
