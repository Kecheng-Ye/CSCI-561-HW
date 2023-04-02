package FOLParser.FOLToken;

import java.util.regex.Pattern;

public class SpecItem {
    public final FOLTokenType type;
    public final Pattern regex;

    SpecItem(FOLTokenType type, Pattern regex) {
        this.type = type;
        this.regex = regex;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}