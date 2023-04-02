package FOLExpression;

public abstract class FOLExpressionNode {
    public final FOLExpressionNodeType type;

    public FOLExpressionNode(final FOLExpressionNodeType type) {
        this.type = type;
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);
}
