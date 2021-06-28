package be.fitnessTracker.dal.async;

import be.fitnessTracker.dal.FriendsRepository;
import be.fitnessTracker.internal.AsyncUtil;
import be.fitnessTracker.models.db.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.concurrent.Callable;

@Repository
public class FriendsRepositoryAsync {
    @Autowired
    private FriendsRepository friendsRepository;

    public Mono<Iterable<Friend>> findUserFriends(String userId) {
        return async(() -> friendsRepository.findAllByUserId(userId));
    }

    public Mono<Integer> save(Friend f1, Friend f2) {
        return async(() -> friendsRepository.save(f1, f2));
    }

    private <T> Mono<T> async(Callable<T> callable) {
        return AsyncUtil.runOnBoundedElasticScheduler(callable);
    }
}
