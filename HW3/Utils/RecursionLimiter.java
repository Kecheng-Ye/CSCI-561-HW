package Utils;

public class RecursionLimiter {
    public static int maxLevel = 1000;

    public static void emerge() {
        if (maxLevel == 0)
            return;
        try {
            throw new IllegalStateException("Too deep, emerging");
        } catch (IllegalStateException e) {
            if (e.getStackTrace().length > maxLevel + 1)
                throw e;
        }
    }
}