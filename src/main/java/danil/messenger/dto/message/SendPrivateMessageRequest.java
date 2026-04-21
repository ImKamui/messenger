package danil.messenger.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendPrivateMessageRequest {

    @NotNull(message = "ID получателя обязателен")
    private int recipientId;

    @NotBlank(message = "Содержание сообщения не может быть пустым")
    private String content;
}
