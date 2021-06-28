package be.fitnessTracker.dal;

import be.fitnessTracker.models.db.Friend;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface FriendsRepository extends PagingAndSortingRepository<Friend, String> {
    @Query(value = "SELECT * FROM tbl_friends f WHERE f.friend_of = :userId", nativeQuery = true)
    Iterable<Friend> findAllByUserId(String userId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO tbl_friends (friend_email, friend_id, friend_username, friend_of) " +
            "VALUES (:#{#f1.friendEmail}, :#{#f1.friendId}, :#{#f1.friendUsername}, :#{#f1.friendOf}), " +
            "(:#{#f2.friendEmail}, :#{#f2.friendId}, :#{#f2.friendUsername}, :#{#f2.friendOf})", nativeQuery = true)
    Integer save(@Param("f1") Friend f1, @Param("f2") Friend f2);
}