package PenteGame;

import java.util.Iterator;

public class PenteGameCoordinateIterator implements Iterator<PenteGameCoordinate> {
    private int currentIdx = 0;

    @Override
    public boolean hasNext() {
        return currentIdx < (PenteGame.BOARD_HEIGHT * PenteGame.BOARD_WIDTH);
    }

    @Override
    public PenteGameCoordinate next() {
        return PenteGameCoordinate.getCoordinate(currentIdx / PenteGame.BOARD_WIDTH, currentIdx++ % PenteGame.BOARD_WIDTH);
    }
}
