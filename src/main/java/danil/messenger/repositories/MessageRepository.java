package danil.messenger.repositories;

import danil.messenger.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    Page<Message> findByChatIdOrderByCreatedAtAsc(int chatId, Pageable pageable);

    Page<Message> findByChatIdOrderByCreatedAtDesc(int chatId, Pageable pageable);
}