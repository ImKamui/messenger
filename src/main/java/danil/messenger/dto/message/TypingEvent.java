package danil.messenger.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypingEvent {
    private Integer userId;
    private String username;
    private Boolean isTyping;
}