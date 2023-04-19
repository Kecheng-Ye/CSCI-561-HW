package KnowledgeBase;

import FOLExpression.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Substitution {
    enum Status {
        SUCCESS,
        FAILURE
    }

    public final Status status;
    public final Map<TermNode, TermNode> assignmentList;

    private Substitution(Status status, Map<TermNode, TermNode> assignmentList) {
        this.status = status;
        this.assignmentList = assignmentList;
    }

    public static Substitution success(Map<TermNode, TermNode> assignmentList) {
        return new Substitution(Status.SUCCESS, assignmentList);
    }

    public static Substitution failure() {
        return new Substitution(Status.FAILURE, null);
    }

    public FOLExpressionNode apply(final FOLExpressionNode node) {
        switch (node.type) {
            case PREDICATE:
            case NEGATED_SENTENCE: {
                return applyToSinglePredicate(node);
            }

            case BINARY_SENTENCE: {
                BinaryExpressionNode binaryExpressionNode = (BinaryExpressionNode) node;
                return new BinaryExpressionNode(
                        apply(binaryExpressionNode.left),
                        apply(binaryExpressionNode.right),
                        binaryExpressionNode.operator
                );
            }

            default: {
                throw new RuntimeException();
            }
        }
    }

    private FOLExpressionNode applyToSinglePredicate(final FOLExpressionNode node) {
        assert KnowledgeBaseUtil.isSinglePredicate(node);

        final List<TermNode> arguments = KnowledgeBaseUtil.getSinglePredicateArgs(node);
        final String predicateName = KnowledgeBaseUtil.getSinglePredicateName(node, true);

        final PredicateNode predicateNode = new PredicateNode(
                predicateName,
                arguments.stream()
                        .map(term -> this.assignmentList.getOrDefault(term, term))
                        .collect(Collectors.toList())
        );
        return (node.type == FOLExpressionNodeType.NEGATED_SENTENCE) ? predicateNode.negate() : predicateNode;
    }

    @Override
    public String toString() {
        return "Substitution{" +
                "status=" + status + "\n" +
                ", assignmentList=" + assignmentList + "\n" +
                '}';
    }
}
