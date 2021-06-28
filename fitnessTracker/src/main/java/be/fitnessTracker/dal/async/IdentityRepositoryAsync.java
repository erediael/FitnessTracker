package be.fitnessTracker.dal.async;

import be.fitnessTracker.dal.IdentityRepository;
import be.fitnessTracker.internal.AsyncUtil;
import be.fitnessTracker.internal.Constants.ValidationMessages;
import be.fitnessTracker.models.db.User;
import be.fitnessTracker.models.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

@Repository
public class IdentityRepositoryAsync {

    @Autowired
    private IdentityRepository identityRepository;

    public Mono<User> findById(String id) {
        return async(() -> identityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(ValidationMessages.USER_ID_NOT_FOUND, id))));
    }

    public Mono<User> findByUsername(String username) {
        return async(() -> identityRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format(ValidationMessages.USERNAME_NOT_FOUND, username))));
    }

    public Mono<User> findByEmail(String email) {
        return async(() -> identityRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(ValidationMessages.EMAIL_NOT_FOUND, email))));
    }

    public Mono<User> save(User user) {
        return async(() -> identityRepository.save(user));
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return AsyncUtil.runOnBoundedElasticScheduler(callable);
    }
}
