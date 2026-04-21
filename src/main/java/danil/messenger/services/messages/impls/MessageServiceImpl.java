package danil.messenger.services.messages.impls;

import danil.messenger.dto.mapping.MessageMapper;
import danil.messenger.dto.message.MessageResponse;
import danil.messenger.dto.message.SendMessageRequest;
import danil.messenger.dto.message.SendPrivateMessageRequest;
import danil.messenger.dto.message.UpdateMessageStatusRequest;
import danil.messenger.exceptions.AppException;
import danil.messenger.models.Chat;
import danil.messenger.models.ChatParticipant;
import danil.messenger.models.Message;
import danil.messenger.models.User;
import danil.messenger.repositories.ChatParticipantRepository;
import danil.messenger.repositories.ChatRepository;
import danil.messenger.repositories.MessageRepository;
import danil.messenger.repositories.UserRepository;
import danil.messenger.services.messages.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository, ChatParticipantRepository chatParticipantRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.chatParticipantRepository = chatParticipantRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(int chatId, int userId, SendMessageRequest request) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Чат с ID " + chatId + " не найден."));

        boolean isMember = chatParticipantRepository.existsByChatIdAndUserId(chatId, userId);
        if (!isMember)
        {
            throw new AppException(HttpStatus.FORBIDDEN, "Вы не состоите в этом чате");
        }

        User sender = userRepository.findById(userId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Пользователь не найден."));
        Message message = new Message();
        message.setContent(request.getContent());
        message.setChat(chat);
        message.setUser(sender);
        message.setStatus("SENT");
        messageRepository.save(message);

        return messageMapper.toDto(message);
    }

    @Override
    @Transactional
    public Page<MessageResponse> getMessage(int chatId, int userId, Pageable pageable) {
        if (!chatParticipantRepository.existsById(chatId))
        {
            throw new AppException(HttpStatus.NOT_FOUND, "Чат не найден");
        }

        boolean isMember = chatParticipantRepository.existsByChatIdAndUserId(chatId, userId);
        if (!isMember)
        {
            throw new AppException(HttpStatus.FORBIDDEN, "Вы не состоите в этом чате");
        }

        Page<Message> messages = messageRepository.findByChatIdOrderByCreatedAtAsc(chatId, pageable);

        return messages.map(messageMapper::toDto);
    }

    @Override
    @Transactional
    public MessageResponse updateMessageStatus(int messageId, int userId, UpdateMessageStatusRequest request) {

        Message message = messageRepository.findById(messageId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Сообщение не найдено"));

        int chatId = message.getChat().getId();
        boolean isMember = chatParticipantRepository.existsByChatIdAndUserId(chatId, userId);
        if (!isMember)
        {
            throw new AppException(HttpStatus.FORBIDDEN, "Вы не можете изменять статус этого сообщения");
        }

        message.setStatus(request.getStatus());
        messageRepository.save(message);

        return messageMapper.toDto(message);

    }

    @Override
    @Transactional
    public MessageResponse sendPrivateMessage(int senderId, int recipientId, SendPrivateMessageRequest request) {

        User sender = userRepository.findById(senderId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Отправитель не найден"));

        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Получатель не найден"));

        Chat privateChat = findOrCreatePrivateChat(sender, recipient);

        Message message = Message.builder()
                .content(request.getContent())
                .chat(privateChat)
                .user(sender)
                .recipient(recipient)
                .status("SENT")
                .build();
        messageRepository.save(message);

        return messageMapper.toDto(message);
    }


    private Chat findOrCreatePrivateChat(User user1, User user2)
    {

        if (user1.getId() == user2.getId()) {
            Optional<Chat> existingChat = chatRepository.findPrivateChatBetweenUsers(
                    user1.getId(), user1.getId()
            );

            if (existingChat.isPresent()) {
                return existingChat.get();
            }

            Chat chat = new Chat();
            chat.setName("Избранное");
            chat.setType("PRIVATE");
            chatRepository.save(chat);

            ChatParticipant participant = new ChatParticipant();
            participant.setChat(chat);
            participant.setUser(user1);
            chatParticipantRepository.save(participant);

            return chat;
        }

        Optional<Chat> existingChat = chatRepository.findPrivateChatBetweenUsers(user1.getId(), user2.getId());
        if (existingChat.isPresent())
        {
            return existingChat.get();
        }

        Chat chat = new Chat();
        chat.setName(null);
        chat.setType("PRIVATE");

        chatRepository.save(chat);

        ChatParticipant participant1 = new ChatParticipant();
        participant1.setChat(chat);
        participant1.setUser(user1);

        ChatParticipant participant2 = new ChatParticipant();
        participant2.setChat(chat);
        participant2.setUser(user2);

        chatParticipantRepository.save(participant1);
        chatParticipantRepository.save(participant2);

        return chat;
    }
}
