package FOLParser.FOLToken;

import java.util.regex.Pattern;

public enum FOLTokenType {
    SPACE(Pattern.compile("^\\s+")),
    COMMA(Pattern.compile("^,")),
    NOT(Pattern.compile("^~")),
    // EQUALS,
    AND(Pattern.compile("^&")),
    OR(Pattern.compile("^\\|")),
    INFER(Pattern.compile("^=>")),
    // DOUBLE_INFER,
    // FOR_ALL,
    // THERE_EXIST,
    LEFT_PARENTHESES(Pattern.compile("^\\(")),
    RIGHT_PARENTHESES(Pattern.compile("^\\)")),
    WORD(Pattern.compile("^[A-Z]\\w*")),
    VARIABLE(Pattern.compile("^[a-z]"));

    public final Pattern pattern;

    FOLTokenType(Pattern pattern) {
        this.pattern = pattern;
    }
}
