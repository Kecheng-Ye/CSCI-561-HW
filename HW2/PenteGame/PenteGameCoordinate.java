package PenteGame;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PenteGameCoordinate {
    public int x;
    public int y;

    public PenteGameCoordinate(final int y, final int x) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("%d%c", (PenteGame.BOARD_HEIGHT - y), (char)('A' + x));
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

    private static final Pattern inputPattern = Pattern.compile("(\\d+)([A-T])");

    public static PenteGameCoordinate parseFromStr(String coordinateStr) {
        Matcher matcher = inputPattern.matcher(coordinateStr);
        if (matcher.find()) {
            int y = PenteGame.BOARD_HEIGHT - Integer.parseInt(matcher.group(1));
            int x = matcher.group(2).charAt(0) - 'A';
            return new PenteGameCoordinate(y, x);
        } else {
            throw new RuntimeException();
        }
    }
}
