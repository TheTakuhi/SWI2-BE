package kiedam.chatapp.backend.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Message")
@Table(name = "\"message\"")
public class Message implements Serializable {

    @Id
    @SequenceGenerator(
            name = "message_sequence",
            sequenceName = "message_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "message_sequence"
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "sender_id",
            referencedColumnName = "id",
            nullable = false
    )
    private User sender;

    @ManyToOne
    @JoinColumn(
            name = "chatroom_id",
            referencedColumnName = "id",
            nullable = false
    )
    private Chatroom chatRoom;

    @Column(
            name = "message",
            length = 1000,
            columnDefinition = "TEXT",
            nullable = false
    )
    private String message;

    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE",
            nullable = false
    )
    private LocalDateTime createdAt;

    public Message(User sender, Chatroom chatRoom, String message, LocalDateTime createdAt) {
        this.sender = sender;
        this.chatRoom = chatRoom;
        this.message = message;
        this.createdAt = createdAt;
    }
}
