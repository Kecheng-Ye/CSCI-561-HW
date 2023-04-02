package FOLParser.FOLToken;

public class FOLToken {
    public final FOLTokenType type;
    public final String value;

    public FOLToken(FOLTokenType type, String value) {
        this.type = type;
        this.value = value;
    }
}
