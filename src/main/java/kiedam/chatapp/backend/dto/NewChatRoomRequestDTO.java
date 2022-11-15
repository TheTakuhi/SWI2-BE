package kiedam.chatapp.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewChatRoomRequestDTO {

    private Set<Long> attachedUserIds = new HashSet<>();
    private String title;
}
