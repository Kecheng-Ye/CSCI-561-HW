package FOLParser.FOLToken;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public enum FOLTokenType {
    SPACE,
    COMMA,
    NOT,
    // EQUALS,
    AND,
    OR,
    INFER,
    // DOUBLE_INFER,
    // FOR_ALL,
    // THERE_EXIST,
    LEFT_PARENTHESES,
    RIGHT_PARENTHESES,
    WORD,
    VARIABLE;

    // Token ---> Its matching regex
    public static final List<SpecItem> SPECS = new ArrayList<>() {{
        add(new SpecItem(SPACE, Pattern.compile("^\\s+")));
        add(new SpecItem(COMMA, Pattern.compile("^,")));
        add(new SpecItem(NOT, Pattern.compile("^~")));
        // add(new SpecItem(EQUALS, Pattern.compile("^=")));
        add(new SpecItem(AND, Pattern.compile("^&")));
        add(new SpecItem(OR, Pattern.compile("^\\|")));
        add(new SpecItem(INFER, Pattern.compile("^=>")));
        // add(new SpecItem(DOUBLE_INFER, Pattern.compile("^<=>")));
        // add(new SpecItem(FOR_ALL, Pattern.compile("^∀")));
        // add(new SpecItem(THERE_EXIST, Pattern.compile("^∃")));
        add(new SpecItem(LEFT_PARENTHESES, Pattern.compile("^\\(")));
        add(new SpecItem(RIGHT_PARENTHESES, Pattern.compile("^\\)")));
        add(new SpecItem(WORD, Pattern.compile("^[A-Z]\\w*")));
        add(new SpecItem(VARIABLE, Pattern.compile("^[a-z]")));
    }};
}
