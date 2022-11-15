package kiedam.chatapp.backend.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageNotification extends Notification{
    private String message;
    private String chatroomTitle;
    private Long chatroomId;

    public static MessageNotification createNew(String message, User author, NotificationType type, Chatroom chatroom) {
        MessageNotification mn = new MessageNotification(message, "", null);
        mn.setAuthorId(author.getId());
        mn.setAuthorUsername(author.getUsername());
        mn.setType(type);
        if (chatroom != null) {
            mn.setChatroomTitle(chatroom.getTitle());
            mn.setChatroomId(chatroom.getId());
        }
        return mn;
    }
}
