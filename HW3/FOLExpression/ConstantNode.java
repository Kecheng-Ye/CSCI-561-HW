package FOLExpression;

import java.util.Objects;

public class ConstantNode extends FOLExpressionNode{
    public final String constantName;

    public ConstantNode(String constantName) {
        super(FOLExpressionNodeType.CONSTANT);
        this.constantName = constantName;
    }

    @Override
    public String toString() {
        return "FOLExpressionNode{" +
                "type=" + type +
                ", constantName='" + constantName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConstantNode)) return false;
        ConstantNode that = (ConstantNode) o;
        return constantName.equals(that.constantName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constantName);
    }
}
