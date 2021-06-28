package be.fitnessTracker.internal.Constants;

public class Routes {
    private Routes() {}

    private static final String API = "/api";
    public static final String LOGIN = API + "/login";
    public static final String REGISTER = API + "/register";
    public static final String FORGOT_PASSWORD = API + "/forgotPassword";
    public static final String CHANGE_PASSWORD = API + "/changePassword";
    public static final String FRIENDS = API + "/friends";
    public static final String USERS_ID = API + "/users/:id";
}
