package be.fitnessTracker.models.exceptions;

public class ValidationException extends RuntimeException {
    public ValidationException(String userMessage) {
        super(userMessage);
    }
}