package PenteGame;

import Game.BiPlayerGame;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PenteGame extends BiPlayerGame<PenteGameState, PenteGameAction, PenteGamePlayer> implements Iterable<PenteGameCoordinate> {
    public static final int BOARD_WIDTH = 5;
    public static final int BOARD_HEIGHT = 5;
    public static final int MAX_NUM_PIECES_IN_A_ROW = 3;
    public static final int CAPTURE_RANGE = 4;
    public static final int NUM_OF_CAPTURES_TO_FINISH = 5;

    public PenteGame(PenteGamePlayer self) {
        super(self, PenteGamePlayer.getOpponent(self));
    }

    @Override
    public PenteGameState initialState() {
        return new PenteGameState();
    }

    @Override
    public PenteGamePlayer playerForNextMove(final PenteGameState state) {
        return (state.round % 2 == 0) ? PenteGamePlayer.WHITE_PLAYER : PenteGamePlayer.BLACK_PLAYER;
    }

    @Override
    public List<PenteGameAction> validActions(final PenteGameState state) {
        PenteGamePlayer nextPlayer = playerForNextMove(state);
        List<PenteGameAction> allActions = PenteGameAction.getAllAction(nextPlayer.playerType);

        if (state.round == 2) {
            PenteGameCoordinate firstWhitePieceCoordinate = null;
            for (final PenteGameCoordinate coordinate : this) {
                if (state.board.get(coordinate) == PenteGamePiece.WHITE) {
                    firstWhitePieceCoordinate = coordinate;
                    break;
                }
            }

            final PenteGameCoordinate finalFirstWhitePieceCoordinate = firstWhitePieceCoordinate;
            return allActions
                    .stream()
                    .filter(penteGameAction -> {
                        final PenteGameCoordinate coordinate = penteGameAction.coordinate;

                        return state.board.get(coordinate) == null &&
                                Math.abs(finalFirstWhitePieceCoordinate.y - coordinate.y) >= 3 &&
                                Math.abs(finalFirstWhitePieceCoordinate.x - coordinate.x) >= 3;
                    })
                    .collect(Collectors.toList());
        }

        return allActions
                .stream()
                .filter(penteGameAction -> {
                        final PenteGameCoordinate coordinate = penteGameAction.coordinate;
                        return state.board.get(coordinate) == null;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PenteGameState result(final PenteGameState state, final PenteGameAction action) {
        return PenteGameBoardUtil.placePieceOnBoard(state, action);
    }


    @Override
    public PenteGameTerminationStatus terminalTest(final PenteGameState state) {
        if (state.whiteCaptures >= NUM_OF_CAPTURES_TO_FINISH) {
            return PenteGameTerminationStatus.success(PenteGamePlayer.WHITE_PLAYER);
        } else if (state.blackCaptures >= NUM_OF_CAPTURES_TO_FINISH) {
            return PenteGameTerminationStatus.success(PenteGamePlayer.BLACK_PLAYER);
        } else if (state.numOfEmptySpots == 0) {
            return PenteGameTerminationStatus.draw();
        } else {
            return PenteGameBoardUtil.checkBoardHasConsecutivePieces(state.board);
        }
    }

    @Override
    public Iterator<PenteGameCoordinate> iterator() {
        return new PenteGameCoordinateIterator();
    }
}
