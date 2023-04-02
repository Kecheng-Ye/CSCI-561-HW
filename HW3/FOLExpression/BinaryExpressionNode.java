package FOLExpression;


public class BinaryExpressionNode extends FOLExpressionNode{
    public final FOLExpressionNode left;
    public final FOLExpressionNode right;
    public final FOLBinaryOperator operator;

    public BinaryExpressionNode(FOLExpressionNode left, FOLExpressionNode right, FOLBinaryOperator operator) {
        super(FOLExpressionNodeType.BINARY_SENTENCE);
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "FOLExpressionNode{" +
                "type=" + type +
                ", operator=" + operator +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryExpressionNode)) return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return left.equals(that.left) && right.equals(that.right) && operator == that.operator;
    }
}
