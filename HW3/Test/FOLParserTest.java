package Test;

import FOLExpression.*;
import FOLParser.FOLParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FOLParserTest {
    final static FOLParser parser = new FOLParser();

    @org.junit.jupiter.api.Test
    void parseConstant() {
        final String input = "Stocked(Portabello) | Stocked(Tofu) => Stocked(VeganHamburger)";
        final FOLExpressionNode node = parser.parse(input);
        FOLExpressionUtil.printExpression(node);
        final FOLExpressionNode nodeInCNF = FOLExpressionUtil.convertToCNF(node);
        FOLExpressionUtil.printExpression(nodeInCNF);
    }
}