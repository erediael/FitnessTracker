package be.fitnessTracker.dal;

import be.fitnessTracker.models.db.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface IdentityRepository extends PagingAndSortingRepository<User, String> {

    @Override
    @Query(value = "SELECT * FROM tbl_Users u WHERE u.id = :id", nativeQuery = true)
    Optional<User> findById(String id);

    @Query(value = "SELECT * FROM tbl_Users u WHERE u.username = :username", nativeQuery = true)
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT * FROM tbl_Users u WHERE u.email = :email", nativeQuery = true)
    Optional<User> findByEmail(String email);
}
