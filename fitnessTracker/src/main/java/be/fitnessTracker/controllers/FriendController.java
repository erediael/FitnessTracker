package be.fitnessTracker.controllers;

import be.fitnessTracker.bl.FriendService;
import be.fitnessTracker.internal.Constants.Routes;
import be.fitnessTracker.models.db.Friend;
import be.fitnessTracker.models.requests.AddFriendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    @GetMapping(Routes.FRIENDS)
    public Mono<Iterable<Friend>> getFriendsByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return friendService.getFriendsByUserId(token);
    }

    @PostMapping(Routes.FRIENDS)
    public Mono<Void> addFriend(@RequestBody AddFriendRequest request,
                                @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return friendService.addFriend(request, token);
    }
}
