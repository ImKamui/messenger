package danil.messenger.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    @NotEmpty(message = "Имя пользователя не может быть пустым")
    private String username;

    @Column(name="email")
    @Email
    @NotEmpty(message = "Поле 'E-mail' должно содержать корректный адрес электронной почты.")
    private String email;

    @Column(name = "password")
    private String password;

    @Lob
    @Column(name = "avatar", columnDefinition ="bytea")
    @JdbcTypeCode(SqlTypes.VARBINARY)
    private byte[] avatar;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatParticipant> chats = new ArrayList<>();

}
