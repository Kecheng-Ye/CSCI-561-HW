package MinMaxSearchSolver;

import Game.*;

public interface Heurstics<S extends State, A extends Action, P extends Player, Game extends BiPlayerGame<S, A, P>>{
    // what is the approximate utility of player at certain state in particular game
    public float eval(Game game, S state, P player);
}
