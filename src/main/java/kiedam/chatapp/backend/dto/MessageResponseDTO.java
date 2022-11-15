package kiedam.chatapp.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import kiedam.chatapp.backend.model.Message;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageResponseDTO extends MessageProcessedDTO implements Serializable {
    @JsonProperty("owned")
    private Boolean owned;

    public MessageResponseDTO(Long id, String message, Long senderId, String senderUsername, Long chatRoomId, LocalDateTime createdAt, Boolean owned) {
        super(id, message, senderId, senderUsername, chatRoomId, createdAt);
        this.owned = owned;
    }

    public static MessageResponseDTO createFromMessage(Message message) {
        return new MessageResponseDTO(
                message.getId(),
                message.getMessage(),
                message.getSender().getId(),
                message.getSender().getUsername(),
                message.getChatRoom().getId(),
                message.getCreatedAt(),
                null);
    }
}
