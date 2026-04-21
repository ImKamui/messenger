package danil.messenger.controllers;

import danil.messenger.dto.message.MessageResponse;
import danil.messenger.dto.message.SendMessageRequest;
import danil.messenger.dto.message.SendPrivateMessageRequest;
import danil.messenger.dto.message.UpdateMessageStatusRequest;
import danil.messenger.services.JwtService;
import danil.messenger.services.messages.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final JwtService jwtService;

    @Autowired
    public MessageController(MessageService messageService, JwtService jwtService) {
        this.messageService = messageService;
        this.jwtService = jwtService;
    }

    @PostMapping("/chat/{chatId}")
    public ResponseEntity<MessageResponse> sendMessage(@PathVariable int chatId, @Valid @RequestBody SendMessageRequest request, HttpServletRequest httpRequest)
    {
        int userId = extractUserIdFromRequest(httpRequest);
        MessageResponse response = messageService.sendMessage(chatId, userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/chat/{chatId}")
    public Page<MessageResponse> getMessage(@PathVariable int chatId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, HttpServletRequest request)
    {
        int userId = extractUserIdFromRequest(request);
        Pageable pageable = PageRequest.of(page, size);
        Page<MessageResponse> messages = messageService.getMessage(chatId, userId, pageable);

        return messages;
    }

    @PatchMapping("/{messageId}")
    public ResponseEntity<MessageResponse> updateMessageStatus(@PathVariable int messageId, @Valid @RequestBody UpdateMessageStatusRequest request, HttpServletRequest httpRequest)
    {
        int userId = extractUserIdFromRequest(httpRequest);
        MessageResponse response = messageService.updateMessageStatus(messageId, userId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat/private/{recipientId}")
    public ResponseEntity<MessageResponse> sendPrivateMessage(@PathVariable("recipientId") int recipientId, @Valid @RequestBody SendPrivateMessageRequest request, HttpServletRequest httpRequest)
    {
        int senderId = extractUserIdFromRequest(httpRequest);

        MessageResponse response = messageService.sendPrivateMessage(senderId, recipientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    private int extractUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        return jwtService.extractUserId(token);
    }
}
