package danil.messenger.dto.message;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponse {

    private int id;
    private String content;
    private String status;
    private LocalDateTime createdAt;
    private int senderId;
    private String senderUsername;
    private int chatId;

}
