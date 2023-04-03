package FOLExpression;

interface printFunctionInterface {
    void print();
}

public class FOLExpressionUtil {
    private static int numOfIndent;

    private static void printIndent(int numOfIndent) {
        System.out.print("\t".repeat(numOfIndent));
    }

    private static void printExpressionNodeTemplate(FOLExpressionNodeType type, printFunctionInterface ...reprs) {
        printIndent(numOfIndent++);
        System.out.println("{");
        printIndent(numOfIndent);
        System.out.printf("type: %s%n", type);
        for (final printFunctionInterface repr : reprs) {
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

    public static FOLExpressionNode convertToCNF(final FOLExpressionNode expressionNode) {
        FOLExpressionNode result;
        result = changeInferToOR(expressionNode);
        result = distributeNOT(result, false);
        result = distributeANDOverOR(result);
        return result;
    }

    private static FOLExpressionNode changeInferToOR(final FOLExpressionNode expressionNode) {
        switch (expressionNode.type) {
            case PREDICATE: {
                return expressionNode;
            }

            case NEGATED_SENTENCE: {
                NegatedSentenceNode node = (NegatedSentenceNode) expressionNode;
                return new NegatedSentenceNode(changeInferToOR(node.body));
            }

            case BINARY_SENTENCE: {
                BinaryExpressionNode node = (BinaryExpressionNode) expressionNode;
                if (node.operator == FOLBinaryOperator.INFER) {
                    return new BinaryExpressionNode(
                            new NegatedSentenceNode(changeInferToOR(node.left)),
                            changeInferToOR(node.right),
                            FOLBinaryOperator.OR
                    );
                }
                return new BinaryExpressionNode(changeInferToOR(node.left), changeInferToOR(node.right), node.operator);
            }

            default: {
                throw new RuntimeException("Cannot handle this type of FOL node type");
            }
        }
    }

    private static FOLExpressionNode distributeNOT(final FOLExpressionNode expressionNode, final boolean isNegated) {
        switch (expressionNode.type) {
            case PREDICATE: {
                if (isNegated) {
                    return new NegatedSentenceNode(expressionNode);
                }
                return expressionNode;
            }

            case NEGATED_SENTENCE: {
                NegatedSentenceNode node = (NegatedSentenceNode) expressionNode;
                if (isNegated) {
                    return distributeNOT(node.body, false);
                }
                return distributeNOT(node.body, true);
            }

            case BINARY_SENTENCE: {
                BinaryExpressionNode node = (BinaryExpressionNode) expressionNode;
                assert node.operator == FOLBinaryOperator.AND || node.operator == FOLBinaryOperator.OR;

                final FOLBinaryOperator negatedOperator = node.operator == FOLBinaryOperator.AND ? FOLBinaryOperator.OR : FOLBinaryOperator.AND;

                return new BinaryExpressionNode(distributeNOT(node.left, isNegated), distributeNOT(node.right, isNegated), isNegated ? negatedOperator : node.operator);
            }

            default: {
                throw new RuntimeException("Cannot handle this type of FOL node type");
            }
        }
    }

    private static FOLExpressionNode distributeANDOverOR(final FOLExpressionNode expressionNode) {
        switch (expressionNode.type) {
            case PREDICATE: {
                return expressionNode;
            }

            case NEGATED_SENTENCE: {
                NegatedSentenceNode node = (NegatedSentenceNode) expressionNode;
                assert node.body.type == FOLExpressionNodeType.PREDICATE;
                return new NegatedSentenceNode(distributeANDOverOR(node.body));
            }

            case BINARY_SENTENCE: {
                BinaryExpressionNode node = (BinaryExpressionNode) expressionNode;
                assert node.operator == FOLBinaryOperator.AND || node.operator == FOLBinaryOperator.OR;

                final FOLExpressionNode left = node.left;
                final FOLExpressionNode right = node.right;

                if ((left.type == FOLExpressionNodeType.PREDICATE || left.type == FOLExpressionNodeType.NEGATED_SENTENCE) &&
                    (right.type == FOLExpressionNodeType.PREDICATE || right.type == FOLExpressionNodeType.NEGATED_SENTENCE)
                ) {
                    // return new BinaryExpressionNode(distributeANDOverOR(left), distributeANDOverOR(right), node.operator);
                    return expressionNode;
                }

                if (right.type == FOLExpressionNodeType.PREDICATE) {
                    // always distribute left over right
                    return distributeANDOverOR(new BinaryExpressionNode(right, left, node.operator));
                }

                if (right.type == FOLExpressionNodeType.NEGATED_SENTENCE) {
                    NegatedSentenceNode rightTemp = (NegatedSentenceNode) right;
                    assert rightTemp.body.type == FOLExpressionNodeType.PREDICATE;
                    // always distribute left over right
                    return distributeANDOverOR(new BinaryExpressionNode(right, left, node.operator));
                }

                assert right.type == FOLExpressionNodeType.BINARY_SENTENCE;
                BinaryExpressionNode rightTemp = (BinaryExpressionNode) right;

                // A V (B ^ C) = (A V B) ^ (A V C)
                if (node.operator == FOLBinaryOperator.OR && rightTemp.operator == FOLBinaryOperator.AND) {
                    return new BinaryExpressionNode(
                            distributeANDOverOR(new BinaryExpressionNode(left, rightTemp.left, FOLBinaryOperator.OR)),
                            distributeANDOverOR(new BinaryExpressionNode(left, rightTemp.right, FOLBinaryOperator.OR)),
                            FOLBinaryOperator.AND
                    );
                }

                return new BinaryExpressionNode(
                        distributeANDOverOR(left),
                        distributeANDOverOR(right),
                        node.operator
                );
            }

            default: {
                throw new RuntimeException("Cannot handle this type of FOL node type");
            }
        }
    }
}
