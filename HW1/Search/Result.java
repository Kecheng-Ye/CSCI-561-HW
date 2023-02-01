package Search;

public class Result<T extends Solution> {
    enum Type {
        SUCCESS,
        FAILURE
    }

    Type resultType;
    T solution;

    private Result(Type type, T solution) {
        this.resultType = type;
        this.solution = solution;
    }

    public static <T extends Solution> Result<T> failedResult() {
        return new Result<>(Type.FAILURE, null);
    }

    public static <T extends Solution> Result<T> successfulResult(T solution) {
        return new Result<>(Type.SUCCESS, solution);
    }

    @Override
    public String toString() {
        if (resultType == Type.FAILURE) {
            return "FAILED";
        } else {
            return String.format("SUCCESS with Solution = %s", solution);
        }
    }
}
