package danil.messenger.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class RegDto {

    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String username;

    @Email
    @NotEmpty(message = "Поле 'E-mail' должно содержать корректный адрес электронной почты.")
    private String email;

    @NotEmpty(message = "Пароль пользователя не может быть пустым")
    private String password;
}
