package PenteGame;

public enum PenteGamePiece {
    WHITE,
    BLACK;

    private static final String WHITE_PLAYER_STRING = "WHITE";
    private static final String BLACK_PLAYER_STRING = "BLACK";

    public static PenteGamePiece parseFromStr(final String str) {
        if (str.equals(WHITE_PLAYER_STRING)) {
            return WHITE;
        } else if (str.equals(BLACK_PLAYER_STRING)) {
            return BLACK;
        } else {
            throw new RuntimeException("Cannot parse string to correct PenteGamePiece");
        }
    }

    @Override
    public String toString() {
        return (this == WHITE) ? WHITE_PLAYER_STRING : BLACK_PLAYER_STRING;
    }
}
