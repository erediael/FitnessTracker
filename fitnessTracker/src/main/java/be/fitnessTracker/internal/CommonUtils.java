package be.fitnessTracker.internal;

public class CommonUtils {
    public static boolean isNullOrWhiteSpace(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }
}
