package danil.messenger.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String username;

    @Email
    @NotEmpty(message = "Поле 'E-mail' должно содержать корректный адрес электронной почты.")
    private String email;

    private String password;
}
