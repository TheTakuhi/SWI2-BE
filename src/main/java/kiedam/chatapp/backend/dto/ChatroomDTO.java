package kiedam.chatapp.backend.dto;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatroomDTO {
    private Long id;
    private Set<UserDTO> users = new HashSet<>();
    private Integer newMessagesCount = 0;
}
