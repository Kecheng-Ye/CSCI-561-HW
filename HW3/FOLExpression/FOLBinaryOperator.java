package FOLExpression;

import FOLParser.FOLToken.FOLToken;
import FOLParser.FOLToken.FOLTokenType;

public enum FOLBinaryOperator {
    AND,
    OR,
    INFER;

    public static FOLBinaryOperator fromFOLToken(final FOLToken token) {
        assert token.type == FOLTokenType.AND ||
               token.type == FOLTokenType.OR  ||
               token.type == FOLTokenType.INFER;

        return token.type == FOLTokenType.AND ? AND :
               token.type == FOLTokenType.OR ? OR   :
               INFER;
    }
}
