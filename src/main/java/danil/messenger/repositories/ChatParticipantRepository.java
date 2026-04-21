package danil.messenger.repositories;

import danil.messenger.models.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Integer> {

    boolean existsByChatIdAndUserId(int chatId, int userId);
}