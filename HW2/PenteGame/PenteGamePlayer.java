package PenteGame;

import Game.Player;

import static PenteGame.PenteGamePiece.BLACK_PLAYER_STRING;
import static PenteGame.PenteGamePiece.WHITE_PLAYER_STRING;

public class PenteGamePlayer extends Player {
    public final PenteGamePiece playerType;

    private PenteGamePlayer(final PenteGamePiece playerType) {
        this.playerType = playerType;
    }

    public static final PenteGamePlayer WHITE_PLAYER = new PenteGamePlayer(PenteGamePiece.WHITE);
    public static final PenteGamePlayer BLACK_PLAYER = new PenteGamePlayer(PenteGamePiece.BLACK);

    public static PenteGamePlayer getOpponent(final PenteGamePlayer self) {
        return (self == WHITE_PLAYER) ? BLACK_PLAYER : WHITE_PLAYER;
    }

    public static PenteGamePlayer parseFromStr(final String playerStr) {
        if (playerStr.equals(WHITE_PLAYER_STRING)) {
            return WHITE_PLAYER;
        } else if (playerStr.equals(BLACK_PLAYER_STRING)) {
            return BLACK_PLAYER;
        } else {
            throw new RuntimeException("Cannot parse string to correct PenteGamePlayer");
        }
    }

    @Override
    public String toString() {
        if (this == (WHITE_PLAYER)) {
            return "WHITE Player";
        } else if (this == (BLACK_PLAYER)) {
            return "Black Player";
        }
        throw new RuntimeException("Unknown Player");
    }
}
