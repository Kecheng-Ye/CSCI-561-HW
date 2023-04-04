package KnowledgeBase;

import FOLExpression.FOLExpressionNode;
import FOLExpression.FOLExpressionNodeType;
import FOLExpression.NegatedSentenceNode;
import FOLExpression.PredicateNode;

import java.util.HashMap;
import java.util.Map;

public class KnowledgeBase {
    // // Predicate Name ----> ID
    // private final Map<FOLExpressionNode, Integer> predicateIdMapping;
    // // Predicate
    // private final List<Map<>> predicateMapping;
    // private int predicateIdCount;
    //
    // public KnowledgeBase() {
    //     predicateIdMapping = new HashMap<>();
    //     predicateIdCount = 0;
    // }
    //
    // private int getOrSetPredicateId(final FOLExpressionNode expressionNode) {
    //     // only should get one single predicate or a negated predicate
    //     assert expressionNode.type == FOLExpressionNodeType.PREDICATE ||
    //            (expressionNode.type == FOLExpressionNodeType.NEGATED_SENTENCE && ((NegatedSentenceNode)(expressionNode)).body.type == FOLExpressionNodeType.PREDICATE);
    //
    //     if (predicateIdMapping.containsKey(expressionNode)) {
    //         return predicateIdMapping.get(expressionNode);
    //     } else {
    //         predicateIdMapping.put(expressionNode, predicateIdCount);
    //         return predicateIdCount++;
    //     }
    // }

}
