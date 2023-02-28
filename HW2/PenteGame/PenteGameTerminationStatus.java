package PenteGame;

import Game.BiPlayerGameTerminationStatus;

public class PenteGameTerminationStatus extends BiPlayerGameTerminationStatus<PenteGameState, PenteGameAction, PenteGamePlayer> {
    private PenteGameTerminationStatus(final boolean isFinished, final PenteGamePlayer winner) {
        super(isFinished, winner);
    }

    public static PenteGameTerminationStatus success(final PenteGamePlayer winner) {
        return new PenteGameTerminationStatus(true, winner);
    }

    public static PenteGameTerminationStatus fail() {
        return new PenteGameTerminationStatus(false, null);
    }

    public static PenteGameTerminationStatus draw() {
        return new PenteGameTerminationStatus(true, null);
    }
}
