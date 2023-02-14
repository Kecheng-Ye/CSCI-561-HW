package PenteGame;

import Game.BiPlayerGame;

import java.util.List;
import java.util.stream.Collectors;

public class PenteGame extends BiPlayerGame<PenteGameState, PenteGameAction, PenteGamePlayer> {
    public static final int BOARD_WIDTH = 19;
    public static final int BOARD_HEIGHT = 19;

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
        return PenteGameBoardUtil.checkGameIsFinished(state.board).isFinished ||
                (state.whiteCaptures >= 5) ||
                (state.blackCaptures >= 5);
    }

    @Override
    public float utility(final PenteGameState state) {
        PenteGamePiece winner = null;
        if (state.whiteCaptures >= 5) {
            winner = PenteGamePiece.WHITE;
        } else if (state.blackCaptures >= 5) {
            winner = PenteGamePiece.BLACK;
        } else {
            PenteGameBoardUtil.PenteGameTerminationStatus status = PenteGameBoardUtil.checkGameIsFinished(state.board);
            assert status.isFinished;
            winner = status.winner;
        }

        return (winner == this.MAX_PLAYER.playerType) ? 1 : -1;
    }
}
