package PenteGame;

import Game.BiPlayerGame;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PenteGame extends BiPlayerGame<PenteGameState, PenteGameAction, PenteGamePlayer> implements Iterable<PenteGameCoordinate> {
    public static final int BOARD_WIDTH = 7;
    public static final int BOARD_HEIGHT = 7;
    public static final int MAX_NUM_PIECES_IN_A_ROW = 4;
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

        return allActions
                .stream()
                .filter(penteGameAction -> {
                        final PenteGameCoordinate coordinate = penteGameAction.coordinate;
                        return state.board[coordinate.y][coordinate.x] == null;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PenteGameState result(final PenteGameState state, final PenteGameAction action) {
        return PenteGameBoardUtil.placePieceOnBoard(state, action);
    }

    @Override
    public boolean terminalTest(final PenteGameState state) {
        return (state.whiteCaptures >= NUM_OF_CAPTURES_TO_FINISH)   ||
               (state.blackCaptures >= NUM_OF_CAPTURES_TO_FINISH)   ||
               (state.numOfEmptySpots == 0) ||
               PenteGameBoardUtil.checkBoardHasConsecutivePieces(state.board).isFinished;
    }

    @Override
    public float utility(final PenteGameState state) {
        PenteGamePiece winner = null;
        if (state.whiteCaptures >= NUM_OF_CAPTURES_TO_FINISH) {
            winner = PenteGamePiece.WHITE;
        } else if (state.blackCaptures >= NUM_OF_CAPTURES_TO_FINISH) {
            winner = PenteGamePiece.BLACK;
        } else if (state.numOfEmptySpots == 0) {
            return 0f;
        }else {
            PenteGameBoardUtil.PenteGameTerminationStatus status = PenteGameBoardUtil.checkBoardHasConsecutivePieces(state.board);
            assert status.isFinished;
            winner = status.winner;
        }

        return (winner == this.MAX_PLAYER.playerType) ? 1 : -1;
    }


    @Override
    public Iterator<PenteGameCoordinate> iterator() {
        return new PenteGameCoordinateIterator();
    }
}
