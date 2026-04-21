package danil.messenger.repositories;

import danil.messenger.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE c.type = 'PRIVATE' AND SIZE(c.participants) = 2 AND EXISTS(SELECT 1 FROM ChatParticipant cp WHERE cp.chat = c AND cp.user.id = :userId1) AND EXISTS(SELECT 1 FROM ChatParticipant cp WHERE cp.chat = c AND cp.user.id = :userId2)")
    Optional<Chat> findPrivateChatBetweenUsers(@Param("userId1") int userId1, @Param("userId2") int userId2);
}
