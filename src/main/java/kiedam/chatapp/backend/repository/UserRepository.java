package kiedam.chatapp.backend.repository;

import kiedam.chatapp.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long userId);

    @Query(
            value = "SELECT * FROM \"user\" WHERE username = :username LIMIT 1",
            nativeQuery = true)
    Optional<User> findUserByUsername(@Param("username") String username);
}
