package FOLExpression;

import java.util.List;
import java.util.Objects;

public class PredicateNode extends FOLExpressionNode{
    public final String predicateName;
    public final List<TermNode> arguments;

    public PredicateNode(final String predicateName, final List<TermNode> arguments) {
        super(FOLExpressionNodeType.PREDICATE);
        this.predicateName = predicateName;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        return "FOLExpressionNode{" +
                "type=" + type +
                ", predicateName='" + predicateName + '\'' +
                ", arguments=" + arguments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PredicateNode)) return false;
        PredicateNode that = (PredicateNode) o;
        return arguments.equals(that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arguments);
    }
}
