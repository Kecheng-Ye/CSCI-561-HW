package KnowledgeBase;

import FOLExpression.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Substitution {
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

public class KnowledgeBaseUnifier {
    public static Substitution unifyTwoPredicates(final FOLExpressionNode x, final FOLExpressionNode y) {
        final String xPredName = KnowledgeBaseUtil.getSinglePredicateName(x);
        final List<TermNode> xPredArgs = KnowledgeBaseUtil.getSinglePredicateArgs(x);

        final String yPredName = KnowledgeBaseUtil.getSinglePredicateName(y);
        final List<TermNode> yPredArgs = KnowledgeBaseUtil.getSinglePredicateArgs(y);

        if (xPredName.equals(yPredName)) {
            Map<TermNode, TermNode> assignment = new HashMap<>();
            boolean isSuccess = unifyArguments(
                    xPredArgs,
                    yPredArgs,
                    assignment
            );

            if (isSuccess) {
                return Substitution.success(assignment);
            }
        }

        return Substitution.failure();
    }

    private static boolean unifyArguments(final List<TermNode> args1, final List<TermNode> args2, final Map<TermNode, TermNode> result) {
        assert args1.size() == args2.size();
        int n = args1.size();
        for (int i = 0; i < n; i++) {
            if (!unifySingleTerm(args1.get(i), args2.get(i), result)) return false;
        }

        return true;
    }

    private static boolean unifySingleTerm(final TermNode x, final TermNode y, final Map<TermNode, TermNode> result) {
        if (x.equals(y)) {
            return true;
        } else if (x.body.type == FOLExpressionNodeType.VARIABLE) {
            return unifyVar(x, y, result);
        } else if (y.body.type == FOLExpressionNodeType.VARIABLE) {
            return unifyVar(y, x, result);
        }

        return false;
    }

    private static boolean unifyVar(final TermNode var, final TermNode x, final Map<TermNode, TermNode> result) {
        assert var.body.type == FOLExpressionNodeType.VARIABLE;

        if (result.containsKey(var)) {
            return unifySingleTerm(result.get(var), x, result);
        } else if (result.containsKey(x)) {
            return unifySingleTerm(var, result.get(x), result);
        } else {
            result.put(var, x);
            return true;
        }
    }
}
