package FOLExpression;

interface functionInteferce {
    void print();
}

public class FOLExpressionUtil {
    public static int numOfIndent;
    // public static StringBuilder stringBuilder;

    private static void printIndent(int numOfIndent) {
        System.out.print("\t".repeat(numOfIndent));
    }

    private static void printExpressionNodeTemplate(FOLExpressionNodeType type, functionInteferce ...reprs) {
        printIndent(numOfIndent++);
        System.out.println("{");
        printIndent(numOfIndent);
        System.out.printf("type: %s%n", type);
        for (final functionInteferce repr : reprs) {
            printIndent(numOfIndent);
            repr.print();
        }
        printIndent(--numOfIndent);
        System.out.println("}");
    }

    private static void printExpressionHelper(FOLExpressionNode expressionNode) {
        switch (expressionNode.type) {
            case CONSTANT: {
                final ConstantNode node = (ConstantNode) expressionNode;
                printExpressionNodeTemplate(FOLExpressionNodeType.CONSTANT, () -> System.out.printf("Name: %s%n", node.constantName));
                break;
            }

            case VARIABLE: {
                final VariableNode node = (VariableNode) expressionNode;
                printExpressionNodeTemplate(FOLExpressionNodeType.VARIABLE, () -> System.out.printf("Name: %s%n", node.varName));
                break;
            }

            case TERM: {
                final TermNode node = (TermNode) expressionNode;
                printExpressionNodeTemplate(FOLExpressionNodeType.TERM, () -> {
                    System.out.println("body: ");
                    printExpressionHelper(node.body);
                });
                break;
            }

            case PREDICATE: {
                final PredicateNode node = (PredicateNode) expressionNode;
                printExpressionNodeTemplate(FOLExpressionNodeType.PREDICATE,
                        () -> System.out.printf("PredicateName: %s%n", node.predicateName),
                        () -> {
                            System.out.println("arguments: [");
                            numOfIndent++;
                            int count = 0;
                            for (final TermNode term : node.arguments) {
                                printExpressionHelper(term);
                                if (++count < node.arguments.size()) {
                                    printIndent(numOfIndent);
                                    System.out.println(",");
                                }
                            }
                            printIndent(--numOfIndent);
                            System.out.println("]");
                        }
                );
                break;
            }

            case NEGATED_SENTENCE: {
                final NegatedSentenceNode node = (NegatedSentenceNode) expressionNode;
                printExpressionNodeTemplate(FOLExpressionNodeType.NEGATED_SENTENCE, () -> {
                    System.out.println("body: ");
                    printExpressionHelper(node.body);
                });
                break;
            }

            case BINARY_SENTENCE: {
                final BinaryExpressionNode node = (BinaryExpressionNode) expressionNode;
                printExpressionNodeTemplate(FOLExpressionNodeType.BINARY_SENTENCE,
                        () -> System.out.printf("Operator: %s%n", node.operator),
                        () -> {
                            System.out.println("left: ");
                            printExpressionHelper(node.left);
                        },
                        () -> {
                            System.out.println("right: ");
                            printExpressionHelper(node.right);
                        }
                );
                break;
            }

            default: {
                break;
            }
        }
    }

    public static void printExpression(FOLExpressionNode expressionNode) {
        numOfIndent = 0;
        printExpressionHelper(expressionNode);
    }
}
