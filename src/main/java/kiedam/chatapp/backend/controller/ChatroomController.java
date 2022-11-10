package kiedam.chatapp.backend.controller;

import kiedam.chatapp.backend.model.Chatroom;
import kiedam.chatapp.backend.service.ChatroomService;
import kiedam.chatapp.backend.service.MessageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/chatrooms")
public class ChatroomController {
    private final ChatroomService chatroomService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;
    private final ChatroomMapper chatroomMapper;

    @Autowired
    public ChatroomController(ChatroomService chatroomService,
                              MessageService messageService,
                              MessageMapper messageMapper,
                              ChatroomMapper chatroomMapper) {
        this.chatroomService = chatroomService;
        this.messageService = messageService;
        this.messageMapper = messageMapper;
        this.chatroomMapper = chatroomMapper;
    }

    @GetMapping
    public List<ChatroomDTO> getAllChatrooms() {
        return chatroomService
                .getAllChatrooms()
                .stream()
                .map(chatroomMapper::mapToChatroomDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Long createNewChatroom(@RequestBody NewChatRoomRequestDTO requestDTO) {
        Chatroom createdChatroom = chatroomService.createNewChatroom(requestDTO.getAttachedUserIds(), requestDTO.getTitle());
        return createdChatroom.getId();
    }

    @GetMapping("/{chatroomId}/messages")
    public List<MessageResponseDTO> getMessagesInChatroom(@PathVariable Long chatroomId) {
        return messageService
                .getAllByChatroomId(chatroomId)
                .stream()
                .map(messageMapper::mapToMessageResponseDTO)
                .collect(Collectors.toList());
    }
}
