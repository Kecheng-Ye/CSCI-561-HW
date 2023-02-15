package PenteGame;

import Game.State;

import java.util.Arrays;
import java.util.stream.Collectors;

public class PenteGameState extends State {
//    public final ArrayList<PanteGameCompactOneDimensionArray> horizontal;
//    public final ArrayList<PanteGameCompactOneDimensionArray> vertical;
//    public final ArrayList<PanteGameCompactOneDimensionArray> leftDiagonal;
//    public final ArrayList<PanteGameCompactOneDimensionArray> rightDiagonal;
//    public final int round;
//
//    // empty board
//    public PanteGameState() {
//        this.round = 0;
//        this.horizontal = new ArrayList<>();
//        this.vertical = new ArrayList<>();
//        this.leftDiagonal = new ArrayList<>();
//        this.rightDiagonal = new ArrayList<>();
//
//        for (int i = 0; i < PanteGame.BOARD_WIDTH; i++) {
//            horizontal.add(new PanteGameCompactOneDimensionArray());
//            vertical.add(new PanteGameCompactOneDimensionArray());
//            leftDiagonal.add(new PanteGameCompactOneDimensionArray());
//            rightDiagonal.add(new PanteGameCompactOneDimensionArray());
//        }
//
//        for (int i = 0; i < PanteGame.BOARD_WIDTH - 1; i++) {
//            leftDiagonal.add(new PanteGameCompactOneDimensionArray());
//            rightDiagonal.add(new PanteGameCompactOneDimensionArray());
//        }
//    }
//
//    public PanteGameState(final PanteGameState prevState) {
//        this.round = prevState.round;
//        this.horizontal = new ArrayList<>(prevState.horizontal);
//        this.vertical = new ArrayList<>(prevState.vertical);
//        this.leftDiagonal = new ArrayList<>(prevState.leftDiagonal);
//        this.rightDiagonal = new ArrayList<>(prevState.rightDiagonal);
//    }
//
//    public PenteGamePiece[][] generateBoard() {
//        return generateBoardByHorizontal();
//    }
//
//    private PenteGamePiece[][] generateBoardByHorizontal() {
//        PenteGamePiece[][] board = new PenteGamePiece[PanteGame.BOARD_HEIGHT][PanteGame.BOARD_WIDTH];
//
//        for (int i = 0; i < PanteGame.BOARD_HEIGHT; i++) {
//            final PanteGameCompactOneDimensionArray row = this.horizontal.get(i);
//
//            for (final PanteGameCompactTuple tuple : row) {
//                for (int j = tuple.startIdx; j <= tuple.endIdx; j++) {
//                    board[i][j] = tuple.piece;
//                }
//            }
//        }
//
//        return board;
//    }
//
//    private PenteGamePiece[][] generateBoardByVertical() {
//        PenteGamePiece[][] board = new PenteGamePiece[PanteGame.BOARD_HEIGHT][PanteGame.BOARD_WIDTH];
//
//        for (int i = 0; i < PanteGame.BOARD_WIDTH; i++) {
//            final PanteGameCompactOneDimensionArray row = this.horizontal.get(i);
//
//            for (final PanteGameCompactTuple tuple : row) {
//                for (int j = tuple.startIdx; j <= tuple.endIdx; j++) {
//                    board[j][i] = tuple.piece;
//                }
//            }
//        }
//
//        return board;
//    }
//
//    private PenteGamePiece[][] generateBoardByLeftDiagonal() {
//        PenteGamePiece[][] board = new PenteGamePiece[PanteGame.BOARD_HEIGHT][PanteGame.BOARD_WIDTH];
//
//        for (int i = 0; i < (PanteGame.BOARD_WIDTH + PanteGame.BOARD_HEIGHT - 1); i++) {
//            final PanteGameCompactOneDimensionArray row = this.horizontal.get(i);
//            int startY = Math.max(0, 18 - i);
//            int startX = Math.max(0, i - 18);
//
//            for (final PanteGameCompactTuple tuple : row) {
//                for (int j = tuple.startIdx; j <= tuple.endIdx; j++) {
//                    board[startY + j][startX + j] = tuple.piece;
//                }
//            }
//        }
//
//        return board;
//    }
//
//    private PenteGamePiece[][] generateBoardByRightDiagonal() {
//        PenteGamePiece[][] board = new PenteGamePiece[PanteGame.BOARD_HEIGHT][PanteGame.BOARD_WIDTH];
//
//        for (int i = 0; i < (PanteGame.BOARD_WIDTH + PanteGame.BOARD_HEIGHT - 1); i++) {
//            final PanteGameCompactOneDimensionArray row = this.horizontal.get(i);
//            int startY = Math.max(0, i - 18);
//            int startX = Math.min(18, i);
//
//            for (final PanteGameCompactTuple tuple : row) {
//                for (int j = tuple.startIdx; j <= tuple.endIdx; j++) {
//                    board[startY + j][startX - j] = tuple.piece;
//                }
//            }
//        }
//
//        return board;
//    }
//
//    public String boardStr() {
//        StringBuilder builder = new StringBuilder();
//        final PenteGamePiece[][] board = generateBoard();
//        for (int i = 0; i < PanteGame.BOARD_HEIGHT; i++) {
//            for (int j = 0; j < PanteGame.BOARD_WIDTH; j++) {
//                if (board[i][j] == null) {
//                    builder.append('.');
//                } else if (board[i][j] == PenteGamePiece.WHITE) {
//                    builder.append('W');
//                } else {
//                    builder.append('B');
//                }
//
//                builder.append(' ');
//            }
//            builder.append('\n');
//        }
//
//        return builder.toString();
//    }

//    @Override
//    public String toString() {
//
//    }

    public final PenteGamePiece[][] board;
    public final int round;
    public int whiteCaptures;
    public int blackCaptures;
    public int numOfEmptySpots;

    private PenteGameState(final PenteGamePiece[][] board, final int round, final int whiteCaptures, final int blackCaptures, final int numOfEmptySpots) {
        this.board = new PenteGamePiece[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            System.arraycopy(board[i], 0, this.board[i], 0, PenteGame.BOARD_WIDTH);
        }
        this.round = round;
        this.whiteCaptures = whiteCaptures;
        this.blackCaptures = blackCaptures;
        this.numOfEmptySpots = numOfEmptySpots;
    }

    public static PenteGameState fromPrevState(final PenteGameState prevState) {
        return new PenteGameState(
                prevState.board,
                prevState.round + 1,
                prevState.whiteCaptures,
                prevState.blackCaptures,
                prevState.numOfEmptySpots
        );
    }

    public PenteGameState() {
        this.round = 0;
        this.board = new PenteGamePiece[PenteGame.BOARD_HEIGHT][PenteGame.BOARD_WIDTH];
        this.whiteCaptures = 0;
        this.blackCaptures = 0;
        this.numOfEmptySpots = PenteGame.BOARD_HEIGHT * PenteGame.BOARD_WIDTH;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("    ");
        for (int j = 0; j < PenteGame.BOARD_WIDTH; j++) {
            builder.append(String.format("%c ", (char)('A' + j)));
        }
        builder.append("\n");

        for (int i = 0; i < PenteGame.BOARD_HEIGHT; i++) {
            builder.append(String.format("%3d ", (PenteGame.BOARD_HEIGHT - i)));
            for (int j = 0; j < PenteGame.BOARD_WIDTH; j++) {
                if (board[i][j] == null) {
                    builder.append('.');
                } else if (board[i][j] == PenteGamePiece.WHITE) {
                    builder.append('W');
                } else {
                    builder.append('B');
                }

                builder.append(' ');
            }
            builder.append('\n');
        }

        return builder.toString();
    }
}
