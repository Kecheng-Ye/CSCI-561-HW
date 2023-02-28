package PenteGame;

import java.util.Arrays;

public class PenteGameBoard {
    private final int[] whitePieces;
    private final int[] blackPieces;

    public PenteGameBoard() {
        this.whitePieces = new int[PenteGame.BOARD_HEIGHT];
        this.blackPieces = new int[PenteGame.BOARD_HEIGHT];
    }

    public PenteGameBoard(final PenteGameBoard board) {
        this.whitePieces = new int[PenteGame.BOARD_HEIGHT];
        this.blackPieces = new int[PenteGame.BOARD_HEIGHT];

        System.arraycopy(board.whitePieces, 0, whitePieces, 0, PenteGame.BOARD_HEIGHT);
        System.arraycopy(board.blackPieces, 0, blackPieces, 0, PenteGame.BOARD_HEIGHT);
    }

    public PenteGamePiece get(final int y, final int x) {
        boolean isWhitePresent = (this.whitePieces[y] & (1 << (PenteGame.BOARD_WIDTH - x - 1))) != 0;
        boolean isBlackPresent = (this.blackPieces[y] & (1 << (PenteGame.BOARD_WIDTH - x - 1))) != 0;

        assert !(isWhitePresent && isBlackPresent);
        return isWhitePresent ? PenteGamePiece.WHITE : (isBlackPresent ? PenteGamePiece.BLACK : null);
    }

    public PenteGamePiece get(final PenteGameCoordinate coordinate) {
        return get(coordinate.y, coordinate.x);
    }

    public void put(final int y, final int x, final PenteGamePiece piece) {
        this.whitePieces[y] = putPieceOnEncoding(this.whitePieces[y], x, piece == PenteGamePiece.WHITE);
        this.blackPieces[y] = putPieceOnEncoding(this.blackPieces[y], x, piece == PenteGamePiece.BLACK);
    }

    public void put(final PenteGameCoordinate coordinate, final PenteGamePiece piece) {
        put(coordinate.y, coordinate.x, piece);
    }

    private int putPieceOnEncoding(int prevEncoding, final int x, final boolean isPut) {
        int mask = (1 << (PenteGame.BOARD_WIDTH - x - 1));
        if (!isPut) {
            mask = (1 << (PenteGame.BOARD_WIDTH)) - 1 - mask;
        }

        return isPut ? (prevEncoding | mask) : (prevEncoding & mask);
    }

    public PenteGamePiece[][] toArrRepr() {
        PenteGamePiece[][] result = new PenteGamePiece[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];

        for (PenteGameCoordinateIterator it = new PenteGameCoordinateIterator(); it.hasNext(); ) {
            PenteGameCoordinate coordinate = it.next();
            result[coordinate.y][coordinate.x] = get(coordinate);
        }

        return result;
    }

    @Override
    public String toString() {
        return PenteGameBoardUtil.boardToStr(toArrRepr());
    }

    @Override
    public boolean equals(Object another) {
        if (another == this) {
            return true;
        }

        if (!(another instanceof PenteGameBoard)) {
            return false;
        }

        PenteGameBoard anotherBoard = (PenteGameBoard) another;

        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            if ((this.whitePieces[i] != anotherBoard.whitePieces[i]) ||
                (this.blackPieces[i] != anotherBoard.blackPieces[i])) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.whitePieces) + Arrays.hashCode(this.blackPieces);
    }
}
