package be.fitnessTracker.models.requests;

import lombok.Getter;

@Getter
public class UserRegisterRequest {
    private String email;
    private String username;
    private String password;
}
