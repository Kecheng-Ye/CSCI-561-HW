package MinMaxSearchSolver;

import Game.State;

public interface Heurstics<S extends State>{
    public float eval(S state);
}
