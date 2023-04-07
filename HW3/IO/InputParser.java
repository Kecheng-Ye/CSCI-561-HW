package IO;

import FOLExpression.FOLExpressionNode;
import FOLParser.FOLParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InputParser {
    private static final FOLParser parser = new FOLParser();

    public static class ParserResult {
        public final FOLExpressionNode query;
        public final List<FOLExpressionNode> rules;

        public ParserResult(final FOLExpressionNode query, final List<FOLExpressionNode> rules) {
            this.query = query;
            this.rules = rules;
        }
    }

    public static ParserResult parseFromFile(final String filePath) {
        try {
            File myObj = new File(filePath);
            Scanner fileReader = new Scanner(myObj);

            // First Line
            String queryString = fileReader.nextLine();
            final FOLExpressionNode query = parser.parse(queryString);

            // Second Line
            int numOfRules = readOneInt(fileReader);

            // Remaining Lines
            final List<FOLExpressionNode> rules = IntStream.range(0, numOfRules)
                                                           .mapToObj(i -> parser.parse(fileReader.nextLine()))
                                                           .collect(Collectors.toList());

            return new ParserResult(query, rules);

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int readOneInt(Scanner fileReader) {
        String numberStr = fileReader.nextLine();
        return Integer.parseInt(numberStr);
    }
}
