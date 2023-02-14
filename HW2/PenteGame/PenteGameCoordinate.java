package PenteGame;

import java.util.ArrayList;
import java.util.List;

public class PenteGameCoordinate {
    public final int x;
    public final int y;

    public PenteGameCoordinate(final int y, final int x) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%d%c", (19 - y), (char)('A' + x));
    }

    public static final List<PenteGameCoordinate> allValidCoordinate = new ArrayList<>() {{
        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            for (int j = 0; j < PenteGame.BOARD_HEIGHT; j++) {
                add(new PenteGameCoordinate(i, j));
            }
        }
    }};

    public static PenteGameCoordinate getCoordinate(final int y, final int x) {
        assert isCoordinateValid(y, x);

        return allValidCoordinate.get(PenteGame.BOARD_WIDTH * y + x);
    }

    public static boolean isCoordinateValid(final int y, final int x) {
        return y >= 0 && y < PenteGame.BOARD_HEIGHT &&
               x >= 0 && x < PenteGame.BOARD_WIDTH;
    }
}
