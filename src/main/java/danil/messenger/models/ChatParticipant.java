package danil.messenger.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_participants", uniqueConstraints = @UniqueConstraint(columnNames={"chat_id","user_id"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipant {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn (name="user_id", nullable = false)
    private User user;


}
