package Search;

public enum SearchMethod {
    BFS,
    UNIFORM_COST_SEARCH,
    A_STAR_SEARCH;

    private static final String BFS_STRING = "BFS";
    private static final String UNIFORM_COST_SEARCH_STRING = "UCS";
    private static final String A_STAR_STRING = "A*";

    public SearchSolver solver() {
        switch (this) {
            case BFS: {
                return new BFS();
            }

            case UNIFORM_COST_SEARCH: {
                return new UniformCostSearch();
            }

            default: {
                return null;
            }
        }
    }

    public static SearchMethod fromStr(String methodString) {
        if (methodString.equals(BFS_STRING)) {
            return BFS;
        } else if (methodString.equals(UNIFORM_COST_SEARCH_STRING)) {
            return UNIFORM_COST_SEARCH;
        } else if (methodString.equals(A_STAR_STRING)) {
            return A_STAR_SEARCH;
        } else {
            throw new IllegalArgumentException("Incorrect Searching Method");
        }
    }
}
