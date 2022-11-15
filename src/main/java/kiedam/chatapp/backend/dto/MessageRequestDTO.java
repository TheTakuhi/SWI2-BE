package kiedam.chatapp.backend.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageRequestDTO {
    private String message;
    private Long chatroomId;
}
