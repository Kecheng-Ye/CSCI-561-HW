package FOLExpression;

public abstract class FOLExpressionNode {
    public final FOLExpressionNodeType type;

    public FOLExpressionNode(final FOLExpressionNodeType type) {
        this.type = type;
    }

    public NegatedSentenceNode negate() {
        return new NegatedSentenceNode(this);
    }

    @Override
    public abstract String toString();

    @Override
    public abstract boolean equals(Object obj);
}
