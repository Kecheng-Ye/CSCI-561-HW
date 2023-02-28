package MinMaxSearchSolver;

import Game.*;

public interface Heurstics<S extends State, P extends Player>{
    // what is the approximate utility of player at certain state in particular game
    public float eval(S state, P player);
}
