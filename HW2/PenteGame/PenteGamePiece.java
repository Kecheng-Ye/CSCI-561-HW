package PenteGame;

public enum PenteGamePiece {
    WHITE,
    BLACK;

    static final String WHITE_PLAYER_STRING = "WHITE";
    static final String BLACK_PLAYER_STRING = "BLACK";

    @Override
    public String toString() {
        return (this == WHITE) ? WHITE_PLAYER_STRING : BLACK_PLAYER_STRING;
    }

    public static PenteGamePiece parseFromBoardStr(final char character) {
        if (character == '.') {
            return null;
        } else if (character == 'W') {
            return WHITE;
        } else if (character == 'B') {
            return BLACK;
        };

        throw new IllegalArgumentException("Wrong Input");
    }
}
