package FOLExpression;

import java.util.Objects;

public class NegatedSentenceNode extends FOLExpressionNode{
    public final FOLExpressionNode body;

    public NegatedSentenceNode(FOLExpressionNode body) {
        super(FOLExpressionNodeType.NEGATED_SENTENCE);
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
        if (!(o instanceof NegatedSentenceNode)) return false;
        NegatedSentenceNode that = (NegatedSentenceNode) o;
        return body.equals(that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, body);
    }
}
