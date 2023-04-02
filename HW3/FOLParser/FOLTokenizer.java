package FOLParser;

import FOLParser.FOLToken.FOLToken;
import FOLParser.FOLToken.FOLTokenType;
import FOLParser.FOLToken.SpecItem;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FOLTokenizer {
    private String string;
    private int cursor;

    public FOLTokenizer(final String string) {
        this.string = string;
        this.cursor = 0;
    }

    public void init(final String string) {
        this.string = string;
        this.cursor = 0;
    }

    public boolean hasMoreTokens() {
        return this.cursor < this.string.length();
    }

    public FOLToken getNextToken() {
        if (!hasMoreTokens()) {
            return null;
        }

        final String subInput = this.string.substring(this.cursor);

        for (final SpecItem specItem : FOLTokenType.SPECS) {
            final String tokenValue = match(specItem.regex, subInput);

            if (tokenValue == null) continue;

            // Skip all possible spaces and try to match next token
            if (specItem.type == FOLTokenType.SPACE) {
                return getNextToken();
            }

            return new FOLToken(specItem.type, tokenValue);
        }

        throw new RuntimeException(String.format("Unexpected Token: %s", subInput));
    }

    private String match(final Pattern pattern, final String input) {
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            final String result = matcher.group(0);
            this.cursor += result.length();
            return result;
        } else {
            return null;
        }
    }
}
