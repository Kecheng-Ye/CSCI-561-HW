package FOLExpression;

import java.util.Objects;

public class TermNode extends FOLExpressionNode{
    public final FOLExpressionNode body;

    public TermNode(final FOLExpressionNode body) {
        super(FOLExpressionNodeType.TERM);
        this.body = body;
    }

    @Override
    public String toString() {
        return "FOLExpressionNode{" +
                "type=" + type +
                ", body=" + body +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TermNode)) return false;
        TermNode termNode = (TermNode) o;
        return body.equals(termNode.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }
}
