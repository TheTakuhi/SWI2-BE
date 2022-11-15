package kiedam.chatapp.backend.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Notification {
    private Long authorId;
    private String authorUsername;
    private NotificationType type;

    public static Notification createNew(User author, NotificationType type) {
        return new Notification(author.getId(), author.getUsername(), type);
    }
}
