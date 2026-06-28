package danil.messenger.controllers;

import danil.messenger.dto.UserDto;
import danil.messenger.dto.message.MessageResponse;
import danil.messenger.dto.message.SendMessageRequest;
import danil.messenger.dto.message.SendPrivateMessageRequest;
import danil.messenger.dto.message.TypingEvent;
import danil.messenger.models.User;
import danil.messenger.services.messages.MessageService;
import danil.messenger.services.users.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketMessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    @Autowired
    public WebSocketMessageController(MessageService messageService, SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/chat/{chatId}/send")
    public void sendMessage(@DestinationVariable int chatId, @Payload @Valid SendMessageRequest request, Principal principal)
    {
        UserDto sender = userService.findOneByUsernameContainsIgnoreCase(principal.getName());
        MessageResponse message = messageService.sendMessage(chatId, sender.getId(), request);

        messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
    }

    @MessageMapping("/private/{recipientId}/send")
    public void sendPrivateMessage(@DestinationVariable int recipientId, @Payload @Valid SendPrivateMessageRequest request, Principal principal)
    {
        UserDto sender = userService.findOneByUsernameContainsIgnoreCase(principal.getName());
        MessageResponse message = messageService.sendPrivateMessage(sender.getId(), recipientId, request);

        messagingTemplate.convertAndSendToUser(String.valueOf(sender.getId()), "/queue/messages", message);
    }

    @MessageMapping("/chat/{chatId}/typing")
    public void typing(@DestinationVariable int chatId, Principal principal)
    {
        UserDto user = userService.findOneByUsernameContainsIgnoreCase(principal.getName());

        TypingEvent event = new TypingEvent(user.getId(), user.getUsername(), true);

        messagingTemplate.convertAndSend("/topic/chat/" + chatId + "/typing", event);
    }
}
