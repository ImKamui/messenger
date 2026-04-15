package danil.messenger.repositories;

import danil.messenger.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u.avatar FROM User u WHERE u.id = :id")
    byte[] findAvatarById(@Param("id") int id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
