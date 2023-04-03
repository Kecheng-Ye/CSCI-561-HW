package FOLParser;

import FOLExpression.*;
import FOLParser.FOLToken.FOLToken;
import FOLParser.FOLToken.FOLTokenType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class FOLParsingException extends RuntimeException {
    public FOLParsingException(final String errMsg) {
        super(errMsg);
    }
}

public class FOLParser {
    private final FOLTokenizer tokenizer;
    private FOLToken lookAheadToken;


    public FOLParser() {
        this.tokenizer = new FOLTokenizer(null);
    }

    public FOLExpressionNode parse(final String FOLSentence) {
        // init
        this.tokenizer.init(FOLSentence);
        // get the first token for predictive parsing
        this.lookAheadToken = this.tokenizer.getNextToken();

        return Sentence();
    }

    /*
     * Sentence -> InferSentence
     */
    private FOLExpressionNode Sentence() {
        return InferSentence();
    }

    /*
     * InferSentence -> AND&ORSentence
     *                 | AND&ORSentence INFER AND&ORSentence
     */
    private FOLExpressionNode InferSentence() {
        FOLExpressionNode left = AND_ORSentence();

        while (this.tokenizer.hasMoreTokens() && this.lookAheadToken.type == FOLTokenType.INFER) {
            final FOLToken token = eatToken(FOLTokenType.INFER);
            FOLExpressionNode right = AND_ORSentence();
            left = new BinaryExpressionNode(
                    left,
                    right,
                    FOLBinaryOperator.fromFOLToken(token)
            );
        }

        return left;
    }

    /*
     * AND_ORSentence -> NegatedSentence
     *                 | NegatedSentence AND/OR NegatedSentence
     */
    private FOLExpressionNode AND_ORSentence() {
        FOLExpressionNode left = NegatedSentence();

        while (this.tokenizer.hasMoreTokens() && isAND_OR(this.lookAheadToken.type)) {
            final FOLToken token = eatAND_OR();
            FOLExpressionNode right = NegatedSentence();
            left = new BinaryExpressionNode(
                    left,
                    right,
                    FOLBinaryOperator.fromFOLToken(token)
            );
        }

        return left;
    }

    /*
     * NegatedSentence -> NOT NegatedSentence
     *                  | PrimarySentence
     */
    private FOLExpressionNode NegatedSentence() {
        if (this.lookAheadToken.type == FOLTokenType.NOT) {
            eatToken(FOLTokenType.NOT);
            return new NegatedSentenceNode(NegatedSentence());
        }

        return PrimarySentence();
    }


    /*
     * PrimarySentence -> ParenthesesSentence
     *                  | AtomicSentence
     */
    private FOLExpressionNode PrimarySentence() {
        if (this.lookAheadToken.type == FOLTokenType.LEFT_PARENTHESES) {
            return ParenthesesSentence();
        }

        return AtomicSentence();
    }

    /*
     * ParenthesesSentence -> ( Sentence )
     */
    private FOLExpressionNode ParenthesesSentence() {
        eatToken(FOLTokenType.LEFT_PARENTHESES);
        final FOLExpressionNode result = Sentence();
        eatToken(FOLTokenType.RIGHT_PARENTHESES);
        return result;
    }

    /*
     * AtomicSentence -> Predicate(Term, ...)
     */
    private PredicateNode AtomicSentence() {
        FOLToken token = eatToken(FOLTokenType.WORD);
        return Predicate(token.value);
    }

    /*
    * Term -> Constant
    *       | Variable
    */
    private TermNode Term() {
        switch (this.lookAheadToken.type) {
            case WORD: {
                return new TermNode(Constant());
            }

            case VARIABLE: {
                return new TermNode(Variable());
            }

            default: {
                error(String.format("Unexpected token: %s for Term", this.lookAheadToken.type));
            }
        }

        return null;
    }

    /*
     * Constant
     */
    private ConstantNode Constant() {
        final FOLToken token = eatToken(FOLTokenType.WORD);
        return new ConstantNode(token.value);
    }

    /*
     * Variable
     */
    private VariableNode Variable() {
        final FOLToken varToken = eatToken(FOLTokenType.VARIABLE);
        return new VariableNode(varToken.value);
    }

    /*
     * Predicate (Term , . . .)
     */
    private PredicateNode Predicate(final String name) {
        eatToken(FOLTokenType.LEFT_PARENTHESES);

        List<TermNode> args = new ArrayList<>();
        boolean isFirstArgs = true;

        while(tokenizer.hasMoreTokens()) {
            if (this.lookAheadToken.type == FOLTokenType.RIGHT_PARENTHESES) break;
            if (isFirstArgs) {
                isFirstArgs = false;
            } else {
                eatToken(FOLTokenType.COMMA);
            }
            args.add(Term());
        }

        eatToken(FOLTokenType.RIGHT_PARENTHESES);
        return new PredicateNode(name, args);
    }

    private FOLToken eatToken(FOLTokenType type) {
        final FOLToken token = this.lookAheadToken;

        if (token == null) {
            error(String.format("Unexpected EOF, expected token type: %s", type));
        }

        if (token.type != type) {
            error(String.format("Unexpected token: %s, expected token type: %s", token.type, type));
        }

        // don't forget to fetch next look ahead
        this.lookAheadToken = this.tokenizer.getNextToken();

        return token;
    }

    private FOLToken eatOneOfToken(FOLTokenType ...types) {
        final FOLToken token = this.lookAheadToken;

        if (token == null) {
            error(String.format("Unexpected EOF, expected token type: %s", Arrays.toString(types)));
        }

        boolean hasMatched = false;
        for (final FOLTokenType type : types) {
            if (token.type == type) {
                hasMatched = true;
                break;
            }
        }

        if (!hasMatched) {
            error(String.format("Unexpected token: %s, expected token types: %s", token.type, Arrays.toString(types)));
        }

        // don't forget to fetch next look ahead
        this.lookAheadToken = this.tokenizer.getNextToken();

        return token;
    }

    private boolean isAND_OR(FOLTokenType type) {
        return type == FOLTokenType.OR || type == FOLTokenType.AND;
    }

    private FOLToken eatAND_OR() {
        return eatOneOfToken(
                FOLTokenType.AND,
                FOLTokenType.OR
        );
    }

    private void error(final String errMsg) {
        throw new FOLParsingException(errMsg);
    }
}
