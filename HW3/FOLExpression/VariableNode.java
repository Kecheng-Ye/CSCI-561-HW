package FOLExpression;

import java.util.Objects;

public class VariableNode extends FOLExpressionNode{
    public final String varName;

    public VariableNode(String varName) {
        super(FOLExpressionNodeType.VARIABLE);
        this.varName = varName;
    }

    @Override
    public String toString() {
        return "FOLExpressionNode{" +
                "type=" + type +
                ", varName='" + varName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariableNode)) return false;
        VariableNode that = (VariableNode) o;
        return varName.equals(that.varName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName);
    }
}
