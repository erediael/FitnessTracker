package be.fitnessTracker.bl;

import be.fitnessTracker.dal.async.IdentityRepositoryAsync;
import be.fitnessTracker.internal.CommonUtils;
import be.fitnessTracker.internal.Constants.ValidationMessages;
import be.fitnessTracker.models.db.User;
import be.fitnessTracker.models.exceptions.InternalException;
import be.fitnessTracker.models.exceptions.NotFoundException;
import be.fitnessTracker.models.exceptions.ValidationException;
import be.fitnessTracker.models.requests.*;
import be.fitnessTracker.models.responses.UserLoginResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;
import java.util.stream.Collectors;

@Service
public class IdentityService {
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!#%*^()?&])[A-Za-z\\d@$#!%()^*?&]{8,32}$";
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9]{4,40}$";
    private static final String EMAIL_REGEX = "(.+)@(.+)\\.(.+){1,40}";
    private static final Random RANDOM = new Random();
    private static final long RANDOM_PASSWORD_LENGTH = 20;
    // representation is in ascii allows all lower,upper chars numbers and special chars
    private static final int RANDOM_PASSWORD_LOWER_BOUND_CHARS = 33;
    private static final int RANDOM_PASSWORD_UPPER_BOUND_CHARS = 122;


    @Autowired
    private IdentityRepositoryAsync identityRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public Mono<User> getUserById(String userId, String token) {
        return jwtService.getUserFromDb(token).flatMap(ignore -> identityRepository.findById(userId));
    }

    public Mono<UserLoginResponse> login(UserLoginRequest request) {
        return identityRepository.findByEmail(request.getEmail())
                .flatMap(user -> {
                    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        return Mono.error(new ValidationException(ValidationMessages.BAD_CREDENTIALS));
                    }

                    String jwt = jwtService.generate(user);
                    UserLoginResponse userLoginResponse = new UserLoginResponse(jwt);
                    return Mono.just(userLoginResponse);
                })
                .onErrorResume(err -> {
                    if (err instanceof NotFoundException || err instanceof ValidationException) {
                        return Mono.error(new ValidationException(ValidationMessages.BAD_CREDENTIALS));
                    }

                    return Mono.error(new InternalException());
                });

    }

    public Mono<User> register(UserRegisterRequest request) {
        validateUserRegisterRequest(request);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return identityRepository.save(user)
                .onErrorResume(error -> {
                    if (error.getCause() instanceof ConstraintViolationException) {
                        return Mono.error(new ValidationException(String.format(
                                ValidationMessages.REGISTRATION_CONSTRAINT_VALIDATION, request.getEmail())));
                    }

                    return Mono.error(new InternalException());
                });
    }

    private void validateUserRegisterRequest(UserRegisterRequest request) {
        validateString(request.getUsername(), USERNAME_REGEX, ValidationMessages.USERNAME_REQUIRED, ValidationMessages.USERNAME_VALID);
        validateString(request.getEmail(), EMAIL_REGEX, ValidationMessages.EMAIL_REQUIRED, ValidationMessages.EMAIL_VALID);
        validatePassword(request.getPassword());
    }

    private void validatePassword(String password) {
        validateString(password, PASSWORD_REGEX, ValidationMessages.PASSWORD_REQUIRED, ValidationMessages.PASSWORD_VALID);
    }

    private void validateString(String input, String regex, String requiredError, String validError) {
        if (CommonUtils.isNullOrWhiteSpace(input)) {
            throw new ValidationException(requiredError);
        }

        if (!input.matches(regex)) {
            throw new ValidationException(validError);
        }
    }

    public Mono<Void> forgotPassword(ForgotPasswordRequest request) {
        return identityRepository.findByEmail(request.getEmail())
                .flatMap(user -> {
                    String password = RANDOM
                            .ints(RANDOM_PASSWORD_LENGTH, RANDOM_PASSWORD_LOWER_BOUND_CHARS, RANDOM_PASSWORD_UPPER_BOUND_CHARS)
                            .mapToObj(i -> String.valueOf((char)i))
                            .collect(Collectors.joining());

                    user.setPassword(passwordEncoder.encode(password));
                    return Mono.zip(identityRepository.save(user), Mono.just(password));
                })
                .flatMap(data -> emailService.sendForgotPasswordEmail(data.getT1().getEmail(), data.getT2()));
    }

    public Mono<User> changePassword(ChangePasswordRequest request, String token) {
        return jwtService.getUserFromDb(token)
                .map(user -> {
                    if (request.getOldPassword().matches(user.getPassword())) {
                        throw new ValidationException(ValidationMessages.OLD_PASSWORD_DOES_NOT_MATCH);
                    }

                    validatePassword(request.getNewPassword());
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    return user;
                })
                .flatMap(user -> identityRepository.save(user));
    }
}
