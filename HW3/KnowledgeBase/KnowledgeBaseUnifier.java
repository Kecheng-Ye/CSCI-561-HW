package KnowledgeBase;

import FOLExpression.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KnowledgeBaseUnifier {
    public static void unifyTwoPredicatesPreprocess(final FOLExpressionNode x, final FOLExpressionNode y) {
        final List<TermNode> argsX = KnowledgeBaseUtil.getSinglePredicateArgs(x);
        final List<TermNode> argsY = KnowledgeBaseUtil.getSinglePredicateArgs(y);

        final Set<VariableNode> uniqueVarsForX = argsX.stream()
                                                      .filter(termNode -> termNode.body.type == FOLExpressionNodeType.VARIABLE)
                                                      .map(termNode -> (VariableNode)(termNode.body))
                                                      .collect(Collectors.toSet());

        final Set<VariableNode> uniqueVarsForY = argsY.stream()
                                                      .filter(termNode -> termNode.body.type == FOLExpressionNodeType.VARIABLE)
                                                      .map(termNode -> (VariableNode)(termNode.body))
                                                      .collect(Collectors.toSet());

        final Set<VariableNode> sameVarsOfXandY = uniqueVarsForX.stream()
                                                                .filter(uniqueVarsForY::contains)
                                                                .collect(Collectors.toSet());

        Consumer<VariableNode> changeUnique = (var) -> {
            while (sameVarsOfXandY.contains(var)) {
                var.varName += "-1";
            }
        };

        argsY.forEach(termNode -> {
            if (termNode.body.type == FOLExpressionNodeType.VARIABLE) {
                changeUnique.accept((VariableNode)(termNode.body));
            }
        });
    }

    public static Substitution unifyTwoPredicates(final FOLExpressionNode x, final FOLExpressionNode y) {
        unifyTwoPredicatesPreprocess(x, y);

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
