package be.fitnessTracker.bl;

import be.fitnessTracker.dal.async.FriendsRepositoryAsync;
import be.fitnessTracker.dal.async.IdentityRepositoryAsync;
import be.fitnessTracker.internal.Constants.ValidationMessages;
import be.fitnessTracker.models.db.Friend;
import be.fitnessTracker.models.exceptions.InternalException;
import be.fitnessTracker.models.exceptions.ValidationException;
import be.fitnessTracker.models.requests.AddFriendRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FriendService {
    private static final Integer ADD_FRIEND_UPDATED_ROWS = 2;
    private static final Logger logger = LoggerFactory.getLogger(FriendService.class);

    @Autowired
    private FriendsRepositoryAsync friendsRepository;

    @Autowired
    private IdentityRepositoryAsync identityRepository;

    @Autowired
    private JwtService jwtService;

    // loggedUser is the one that is accepting the friend request
    public Mono<Void> addFriend(AddFriendRequest request, String token) {
        return jwtService.getUserFromDb(token)
                .flatMap(loggedUser -> Mono.zip(Mono.just(loggedUser), identityRepository.findById(request.getUserId())))
                .flatMap(users -> {
                    if (users.getT1().getId().equals(users.getT2().getId())) {
                        return Mono.error(new ValidationException(ValidationMessages.FRIEND_FAILURE));
                    }

                    Friend f1 = Friend.friendFromUser(users.getT1(), users.getT2());
                    Friend f2 = Friend.friendFromUser(users.getT2(), users.getT1());
                    return friendsRepository.save(f1, f2);
                })
                .flatMap(updatedRows -> {
                    if (ADD_FRIEND_UPDATED_ROWS.equals(updatedRows)) {
                        return Mono.empty();
                    }

                    return Mono.error(new InternalException());
                });
    }

    public Mono<Iterable<Friend>> getFriendsByUserId(String token) {
        return jwtService.getUserFromDb(token)
                .flatMap(loggedUser -> friendsRepository.findUserFriends(loggedUser.getId()));
    }
}
