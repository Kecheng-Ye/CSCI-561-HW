package Test;

import FOLExpression.*;
import FOLParser.FOLParser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FOLParserTest {
    final static FOLParser parser = new FOLParser();

    @org.junit.jupiter.api.Test
    void parseConstant() {
        final String input = "~( P(A) | ( P(A) => K(A) ))";
        final FOLExpressionNode node = parser.parse(input);
        // final FOLExpressionNode expected = new BinaryExpressionNode(
        //         new BinaryExpressionNode(
        //                 new PredicateNode("Order", List.of(new TermNode(new VariableNode("x")), new TermNode(new VariableNode("y")))),
        //                 new NegatedSentenceNode(new PredicateNode("Seated",  List.of(new TermNode(new VariableNode("x"))))),
        //                 FOLBinaryOperator.INFER
        //         ),
        //         new PredicateNode("Stocked",  List.of(new TermNode(new VariableNode("y")))),
        //         FOLBinaryOperator.AND
        // );
        System.out.println(node);
        // assertEquals(expected, node);
        FOLExpressionUtil.printExpression(node);
        final FOLExpressionNode nodeInCNF = FOLExpressionUtil.convertToCNF(node);
        FOLExpressionUtil.printExpression(nodeInCNF);
    }
}