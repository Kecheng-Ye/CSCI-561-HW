package SkiProblem;

import Search.BFS;
import Search.SearchSolver;

public enum SearchMethod {
    BFS,
    UNIFORM_COST_SEARCH,
    A_STAR_SEARCH;

    public SearchSolver solver() {
        switch (this) {
            case BFS: {
                return new BFS();
            }

            default: {
                return null;
            }
        }
    }
}
