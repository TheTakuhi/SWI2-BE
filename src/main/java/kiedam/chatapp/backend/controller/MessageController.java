package kiedam.chatapp.backend.controller;

import kiedam.chatapp.backend.model.Message;
import kiedam.chatapp.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageController(
            MessageService messageService,
            MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }

    @PostMapping
    public MessageResponseDTO sendMessage(@RequestBody MessageRequestDTO messageRequestDTO) {
        Message sentMessage = messageService.sendMessage(messageRequestDTO);
        return messageMapper.mapToMessageResponseDTO(sentMessage);
    }
}
