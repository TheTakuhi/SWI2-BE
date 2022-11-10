package kiedam.chatapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kiedam.chatapp.backend.model.Message;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageProcessedDTO implements Serializable {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("message")
    private String message;
    @JsonProperty("senderId")
    private Long senderId;
    @JsonProperty("senderUsername")
    private String senderUsername;
    @JsonProperty("chatRoomId")
    private Long chatRoomId;
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;


    public static MessageProcessedDTO createFromMessage(Message message) {
        return new MessageProcessedDTO(
                message.getId(),
                message.getMessage(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getChatRoom().getId(),
                message.getCreatedAt());
    }
}
