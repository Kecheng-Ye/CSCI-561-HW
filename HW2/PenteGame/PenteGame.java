package PenteGame;

import Game.BiPlayerGame;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class PenteGame extends BiPlayerGame<PenteGameState, PenteGameAction, PenteGamePlayer> implements Iterable<PenteGameCoordinate> {
    public static final int BOARD_WIDTH = 19;
    public static final int BOARD_HEIGHT = 19;
    public static final int MAX_NUM_PIECES_IN_A_ROW = 5;
    public static final int CAPTURE_RANGE = 4;
    public static final int NUM_OF_CAPTURES_TO_FINISH = 5;
    public static final int SECOND_WHITE_PIECE_OUT_RADIUS = 3;

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
            return validActionForRound2(state, allActions);
        }

        final PenteGameCoordinate midPoint =  PenteGameCoordinate.getCoordinate(PenteGame.BOARD_HEIGHT / 2, PenteGame.BOARD_WIDTH / 2);
        final PenteGameCoordinate leftTop = (state.round == 0) ? midPoint : state.board.leftTop;
        final PenteGameCoordinate rightBottom = (state.round == 0) ? midPoint : state.board.rightBottom;

        return allActions
                .stream()
                .filter(penteGameAction -> {
                        final PenteGameCoordinate coordinate = penteGameAction.coordinate;
                        return state.board.get(coordinate) == null &&
                               PenteGameBoardUtil.withInOfSquareRange(
                                       leftTop, rightBottom,
                                       coordinate, 3
                               );
                })
                .collect(Collectors.toList());
    }

    private List<PenteGameAction> validActionForRound2(final PenteGameState state, final List<PenteGameAction> allActions) {
        assert state.round == 2;

        // find the first white piece
        final PenteGameCoordinate firstWhitePieceCoordinate =
                state.board.get(state.board.leftTop) == PenteGamePiece.WHITE ? state.board.leftTop : state.board.rightBottom;

        return allActions
                .stream()
                .filter(penteGameAction -> {
                    final PenteGameCoordinate coordinate = penteGameAction.coordinate;

                    return state.board.get(coordinate) == null &&
                           PenteGameBoardUtil.outOfRange(firstWhitePieceCoordinate, coordinate, SECOND_WHITE_PIECE_OUT_RADIUS) &&
                           PenteGameBoardUtil.withInOfSquareRange(
                                   firstWhitePieceCoordinate, firstWhitePieceCoordinate,
                                   coordinate, 4
                           );
                })
                .collect(Collectors.toList());
    }

    @Override
    public PenteGameState result(final PenteGameState state, final PenteGameAction action) {
        return PenteGameBoardUtil.placePieceOnBoard(state, action);
    }


    @Override
    public PenteGameTerminationStatus terminalTest(final PenteGameState state) {
        if (state.whiteCaptures >= NUM_OF_CAPTURES_TO_FINISH)
            return PenteGameTerminationStatus.success(PenteGamePlayer.WHITE_PLAYER);

        if (state.blackCaptures >= NUM_OF_CAPTURES_TO_FINISH)
            return PenteGameTerminationStatus.success(PenteGamePlayer.BLACK_PLAYER);

        if (state.numOfEmptySpots == 0)
            return PenteGameTerminationStatus.draw();

        if (state.round < (NUM_OF_CAPTURES_TO_FINISH * 2 - 1))
            return PenteGameTerminationStatus.fail();

        return PenteGameBoardUtil.checkBoardHasConsecutivePieces(state.board);
    }

    @Override
    public Iterator<PenteGameCoordinate> iterator() {
        return new PenteGameCoordinateIterator();
    }
}
