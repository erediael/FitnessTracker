package be.fitnessTracker.controllers;

import be.fitnessTracker.bl.IdentityService;
import be.fitnessTracker.internal.Constants.Routes;
import be.fitnessTracker.models.db.User;
import be.fitnessTracker.models.requests.*;
import be.fitnessTracker.models.responses.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class IdentityController {

    @Autowired
    private IdentityService identityService;

    @GetMapping(Routes.USERS_ID)
    public Mono<User> getUserById(@RequestParam String userId, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return identityService.getUserById(userId, token);
    }

    @PostMapping(Routes.LOGIN)
    public Mono<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        return identityService.login(request);
    }

    @PostMapping(Routes.REGISTER)
    public Mono<User> register(@RequestBody UserRegisterRequest request) {
        return identityService.register(request);
    }

    @PostMapping(Routes.FORGOT_PASSWORD)
    public Mono<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return identityService.forgotPassword(request);
    }

    @PostMapping(Routes.CHANGE_PASSWORD)
    public Mono<User> changePassword(@RequestBody ChangePasswordRequest request,
                                     @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return identityService.changePassword(request, token);
    }
}
