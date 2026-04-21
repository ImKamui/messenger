package danil.messenger.services.messages;

import danil.messenger.dto.message.MessageResponse;
import danil.messenger.dto.message.SendMessageRequest;
import danil.messenger.dto.message.SendPrivateMessageRequest;
import danil.messenger.dto.message.UpdateMessageStatusRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {
    /**
     * Отправить сообщение в чат
     * @param chatId ID чата
     * @param userId ID отправителя
     * @param request содержание сообщения
     * @return отправленное сообщение
     */
    MessageResponse sendMessage(int chatId, int userId, SendMessageRequest request);

    /**
     * Отправить сообщение в чат
     * @param chatId ID чата
     * @param userId ID отправителя
     * @param pageable пагинация (размер страницы, номер и т.д.)
     * @return страница с сообщениями
     */
    Page<MessageResponse> getMessage(int chatId, int userId, Pageable pageable);

    /**
     * Обновить статус сообщения
     * @param messageId ID сообщения
     * @param userId ID пользователя (проверяем права)
     * @param request новый статус
     * @return обновлённое сообщение
     */
    MessageResponse updateMessageStatus(int messageId, int userId, UpdateMessageStatusRequest request);

    /**
     * Отправить личное сообщение пользователю
     * Если личного чата нет — создаётся автоматически
     *
     * @param senderId ID отправителя
     * @param recipientId ID получателя
     * @param request содержание сообщения
     * @return отправленное сообщение
     */
    MessageResponse sendPrivateMessage(int senderId, int recipientId, SendPrivateMessageRequest request);
}
