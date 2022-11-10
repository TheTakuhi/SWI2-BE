package kiedam.chatapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewChatRoomDTO {
    private Set<Long> attachedUserIds = new HashSet<>();
    private String title;
}
