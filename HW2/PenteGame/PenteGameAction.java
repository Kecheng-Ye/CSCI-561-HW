package PenteGame;

import Game.Action;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PenteGameAction extends Action {
    public final PenteGameCoordinate coordinate;
    public final PenteGamePiece pieceColor;

    private PenteGameAction(final PenteGameCoordinate coordinate, final PenteGamePiece pieceColor) {
        this.coordinate = coordinate;
        this.pieceColor = pieceColor;
    }

    @Override
    public String toString() {
        return "PenteGameAction{" +
                "coordinate=" + coordinate +
                ", pieceColor=" + pieceColor +
                '}';
    }

    public String actionStrOutput() {
        return coordinate.toString();
    }

    public static final List<PenteGameAction> ALL_BLACK_MOVES = PenteGameCoordinate.allValidCoordinate
            .stream()
            .map(penteGameCoordinate -> new PenteGameAction(penteGameCoordinate, PenteGamePiece.BLACK))
            .collect(Collectors.toList());

    public static final List<PenteGameAction> ALL_WHITE_MOVES = PenteGameCoordinate.allValidCoordinate
            .stream()
            .map(penteGameCoordinate -> new PenteGameAction(penteGameCoordinate, PenteGamePiece.WHITE))
            .collect(Collectors.toList());

    public static List<PenteGameAction> getAllAction(PenteGamePiece pieceColor) {
        return (pieceColor == PenteGamePiece.WHITE) ? ALL_WHITE_MOVES : ALL_BLACK_MOVES;
    }

    public static PenteGameAction parseFromStr(PenteGamePlayer player, String actionStr) {
        return new PenteGameAction(PenteGameCoordinate.parseFromStr(actionStr), player.playerType);
    }

    public void dumpIntoFile(final String filePath) {
        try {
            File outputFile = new File(filePath);
            outputFile.createNewFile();
            FileWriter writer = new FileWriter(filePath);
            writer.write(this.actionStrOutput() + "\n");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PenteGameAction)) return false;
        PenteGameAction action = (PenteGameAction) o;
        return coordinate.equals(action.coordinate) && pieceColor == action.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate, pieceColor);
    }
}
