package PenteGame;

import Game.Action;

import java.util.List;
import java.util.stream.Collectors;

public class PenteGameAction extends Action {
    final PenteGameCoordinate coordinate;
    final PenteGamePiece pieceColor;

    public PenteGameAction(final PenteGameCoordinate coordinate, final PenteGamePiece pieceColor) {
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

}
