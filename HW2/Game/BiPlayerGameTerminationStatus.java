package Game;

public class BiPlayerGameTerminationStatus<S extends State, A extends Action, P extends Player> {
    public final boolean isFinished;
    public final P winner;

    protected BiPlayerGameTerminationStatus(final boolean isFinished, final P winner) {
        this.isFinished = isFinished;
        this.winner = winner;
    }

    public float utility(final P player) {
        assert this.isFinished;
        return this.winner == null ? 0f : ((this.winner == player) ? 1f : -1f);
    }
}
