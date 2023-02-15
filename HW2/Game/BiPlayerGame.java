package Game;
import java.util.ArrayList;
import java.util.List;

public abstract class BiPlayerGame<S extends State, A extends Action, P extends Player> {
    public final P MAX_PLAYER;
    public final P MIN_PLAYER;

    public BiPlayerGame(final P MAX_PLAYER, final P MIN_PLAYER) {
        this.MAX_PLAYER = MAX_PLAYER;
        this.MIN_PLAYER = MIN_PLAYER;
    }

    // Specifies how the game is set up at the start
    public abstract S initialState();

    // Defines which player has the move in a state
    public abstract P playerForNextMove(S state);

    // Returns the set of legal moves in a state
    public abstract List<A> validActions(S state);

    // The transition model, which defines the result of a move
    public abstract S result(S state, A action);

    // A terminal test, which is true when the game is over and false otherwise
    public abstract boolean terminalTest(S state);

    // A utility function (also called an objective function or payoff function) defines the final numeric value
    public abstract float utility(S state);
}
