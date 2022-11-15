package kiedam.chatapp.backend.mapper;

import kiedam.chatapp.backend.dto.ChatroomDTO;
import kiedam.chatapp.backend.model.Chatroom;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.service.AmqpService;
import kiedam.chatapp.backend.service.UserService;
import kiedam.chatapp.backend.utils.RabbitMqBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatroomMapper {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final AmqpService amqpService;

    public ChatroomMapper(ModelMapper modelMapper,
                          UserService userService,
                          AmqpService amqpService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.amqpService = amqpService;
    }

    public ChatroomDTO mapToChatroomDTO(Chatroom chatroom) {
        ChatroomDTO chatroomDTO = modelMapper.map(chatroom, ChatroomDTO.class);
        User loggedUser = userService.getLoggedUser();
        String queueName = RabbitMqBuilder.Name.buildChatroomUserQueue(loggedUser, chatroom);
        Integer messageCount = amqpService.qetQueueCount(queueName);
        chatroomDTO.setNewMessagesCount(messageCount);
        return chatroomDTO;
    }
}
