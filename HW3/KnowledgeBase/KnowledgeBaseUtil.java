package KnowledgeBase;

import FOLExpression.*;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseUtil {
    public static List<FOLExpressionNode> splitCNF(final FOLExpressionNode expressionNode) {
        List<FOLExpressionNode> result = new ArrayList<>();
        splitCNFHelper(expressionNode, result);
        return result;
    }

    private static void splitCNFHelper(final FOLExpressionNode expressionNode, final List<FOLExpressionNode> result) {
        switch (expressionNode.type) {
            case PREDICATE:

            case NEGATED_SENTENCE: {
                result.add(expressionNode);
                return;
            }

            case BINARY_SENTENCE: {
                BinaryExpressionNode node = (BinaryExpressionNode) expressionNode;
                if (node.operator == FOLBinaryOperator.OR) {
                    result.add(expressionNode);
                    return;
                }

                splitCNFHelper(node.left, result);
                splitCNFHelper(node.right, result);
                break;
            }

            default: {
                throw new RuntimeException(String.format("Cannot handle this type of FOL node type %s", expressionNode.type));
            }
        }
    }

    public static List<FOLExpressionNode> splitSentenceToSinglePredicate(final FOLExpressionNode expressionNode) {
        List<FOLExpressionNode> result = new ArrayList<>();
        splitSentenceToSinglePredicateHelper(expressionNode, result);
        return result;
    }

    private static void splitSentenceToSinglePredicateHelper(final FOLExpressionNode expressionNode, final List<FOLExpressionNode> result) {
        switch (expressionNode.type) {
            case PREDICATE: {
                result.add(expressionNode);
                break;
            }

            case NEGATED_SENTENCE: {
                assert ((NegatedSentenceNode)(expressionNode)).body.type == FOLExpressionNodeType.PREDICATE;
                result.add(expressionNode);
                break;
            }

            case BINARY_SENTENCE: {
                BinaryExpressionNode node = (BinaryExpressionNode) expressionNode;
                splitSentenceToSinglePredicateHelper(node.left, result);
                splitSentenceToSinglePredicateHelper(node.right, result);
                break;
            }

            default: {
                throw new RuntimeException(String.format("Cannot handle this type of FOL node type %s", expressionNode.type));
            }
        }
    }

    public static boolean isSinglePredicate(final FOLExpressionNode expressionNode) {
        return expressionNode.type == FOLExpressionNodeType.PREDICATE ||
               (expressionNode.type == FOLExpressionNodeType.NEGATED_SENTENCE && ((NegatedSentenceNode)(expressionNode)).body.type == FOLExpressionNodeType.PREDICATE);
    }

    public static List<TermNode> getArgumentsFromSinglePredicate(final FOLExpressionNode expressionNode) {
        assert isSinglePredicate(expressionNode);

        if (expressionNode.type == FOLExpressionNodeType.PREDICATE) {
            return ((PredicateNode)expressionNode).arguments;
        } else {
            return ((PredicateNode)((NegatedSentenceNode)expressionNode).body).arguments;
        }
    }

    public static String getSinglePredicateName(final FOLExpressionNode expressionNode) {
        assert isSinglePredicate(expressionNode);
        if (expressionNode.type == FOLExpressionNodeType.PREDICATE) {
            return ((PredicateNode)expressionNode).predicateName;
        } else {
            return "~" + ((PredicateNode)((NegatedSentenceNode)expressionNode).body).predicateName;
        }
    }

    public static String getSinglePredicateName(final FOLExpressionNode expressionNode, boolean isPure) {
        if (!isPure) {
            return getSinglePredicateName(expressionNode);
        }

        assert isSinglePredicate(expressionNode);
        if (expressionNode.type == FOLExpressionNodeType.PREDICATE) {
            return ((PredicateNode)expressionNode).predicateName;
        } else {
            return ((PredicateNode)((NegatedSentenceNode)expressionNode).body).predicateName;
        }
    }

    public static List<TermNode> getSinglePredicateArgs(final FOLExpressionNode expressionNode) {
        assert isSinglePredicate(expressionNode);
        if (expressionNode.type == FOLExpressionNodeType.PREDICATE) {
            return ((PredicateNode)expressionNode).arguments;
        } else {
            return ((PredicateNode)((NegatedSentenceNode)expressionNode).body).arguments;
        }
    }

    public static FOLExpressionNode concatTwoNodeWithOr(final FOLExpressionNode left, final  FOLExpressionNode right) {
        assert isSinglePredicate(right);

        if (left == null) {
            return right;
        } else {
            return new BinaryExpressionNode(left, right, FOLBinaryOperator.OR);
        }
    }

    public static FOLExpressionNode negateSinglePredicate(final FOLExpressionNode expressionNode) {
        assert isSinglePredicate(expressionNode);

        if (expressionNode.type == FOLExpressionNodeType.PREDICATE) {
            return new NegatedSentenceNode(expressionNode);
        } else {
            return ((NegatedSentenceNode)expressionNode).body;
        }
    }
}
