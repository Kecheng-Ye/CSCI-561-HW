package Test;

import FOLExpression.FOLExpressionNode;
import FOLParser.FOLParser;
import KnowledgeBase.KnowledgeBaseUnifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeBaseUnifierTest {
    final FOLParser parser = new FOLParser();

    @Test
    void unifyTwoPredicates() {
        final FOLExpressionNode sentenceA = parser.parse("MiniSudoku(Bb,x)");
        final FOLExpressionNode sentenceB = parser.parse("MiniSudoku(x,D)");

        System.out.println(KnowledgeBaseUnifier.unifyTwoPredicates(sentenceA, sentenceB));
    }
}