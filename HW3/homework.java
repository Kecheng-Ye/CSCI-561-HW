import IO.InputParser;
import IO.OutputWritter;
import KnowledgeBase.KnowledgeBase;

public class homework {
    public static void main(String[] args) {
        final String INPUT_FILE_PATH = "input.txt";
        final String OUTPUT_FILE_PATH = "output.txt";


        final KnowledgeBase KB = new KnowledgeBase();
        final InputParser.ParserResult parserResult = InputParser.parseFromFile(INPUT_FILE_PATH);

        assert parserResult != null;
        parserResult.rules.forEach(KB::tell);
        OutputWritter.writeSolutionsToFile(OUTPUT_FILE_PATH, KB.ask(parserResult.query));
    }
}
