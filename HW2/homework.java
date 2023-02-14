import PenteGame.*;

import java.util.List;

public class homework {
    public static void main(String[] args) {
        PenteGame game = new PenteGame(PenteGamePlayer.BLACK_PLAYER);
        PenteGameState initState = game.initialState();
        PenteGameAction action = new PenteGameAction(new PenteGameCoordinate(7, 10), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(9, 10), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(8, 10), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(7, 10), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(11, 10), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(12, 10), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(13, 10), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(10, 9), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(10, 8), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(10, 7), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(10, 11), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(10, 12), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(10, 13), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(9, 9), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(8, 8), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(7, 7), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(11, 11), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(12, 12), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        action = new PenteGameAction(new PenteGameCoordinate(13, 13), PenteGamePiece.WHITE);
        initState = game.result(initState, action);

        System.out.println(initState);

        action = new PenteGameAction(new PenteGameCoordinate(10, 10), PenteGamePiece.BLACK);
        initState = game.result(initState, action);

        System.out.println(initState);
        System.out.println(game.utility(initState));
    }
}
