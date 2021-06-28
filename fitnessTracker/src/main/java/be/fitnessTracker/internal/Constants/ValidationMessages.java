package be.fitnessTracker.internal.Constants;

public class ValidationMessages {
    private ValidationMessages() {}

    public static final String FRIEND_FAILURE = "Can not add yourself as a friend!";
    public static final String BAD_CREDENTIALS = "Bad credentials, please try again!";
    public static final String EMAIL_NOT_FOUND = "User with email: %s not found!";
    public static final String USERNAME_NOT_FOUND = "User with username: %s not found!";
    public static final String USER_ID_NOT_FOUND = "User with id: %s not found!";
    public static final String REGISTRATION_CONSTRAINT_VALIDATION = "User with email: %s already exists!";

    // validators
    public static final String USERNAME_REQUIRED = "Username is required!";
    public static final String USERNAME_VALID = "Username should be between 4 and 40 characters and contain only letters and numbers!";
    public static final String EMAIL_REQUIRED = "Email is required!";
    public static final String EMAIL_VALID = "Email should be valid email(eg: a@a.a) and less than 50 characters!";
    public static final String PASSWORD_REQUIRED = "Password is required!";
    public static final String PASSWORD_VALID = "Password should be between 8 and 32 characters and contain at least 1 upper case letter, 1 lower case letter, 1 number and 1 special symbol!";
    public static final String OLD_PASSWORD_DOES_NOT_MATCH = "Old password does not match the current!";
}
