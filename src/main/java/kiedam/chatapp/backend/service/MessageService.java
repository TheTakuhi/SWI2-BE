package kiedam.chatapp.backend.service;

import kiedam.chatapp.backend.dto.MessageProcessedDTO;
import kiedam.chatapp.backend.dto.MessageRequestDTO;
import kiedam.chatapp.backend.model.Chatroom;
import kiedam.chatapp.backend.model.Message;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.repository.MessageRepository;
import kiedam.chatapp.backend.utils.RabbitMqBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatroomService chatRoomService;
    private final RabbitTemplate rabbitTemplate;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          ChatroomService chatRoomService,
                          RabbitTemplate rabbitTemplate,
                          UserService userService,
                          NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.chatRoomService = chatRoomService;
        this.rabbitTemplate = rabbitTemplate;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public Message createMessage(Message message) {
        message.setCreatedAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getAllByChatroomId(Long chatRoomId) {
        return messageRepository.findAllByChatRoom_Id(chatRoomId);
    }

    public Message sendMessage(MessageRequestDTO messageRequestDTO) {
        Chatroom chatroom = chatRoomService.getChatroomById(messageRequestDTO.getChatroomId());
        User loggedUser = userService.getLoggedUser();

        Message message = new Message(
                loggedUser,
                chatroom,
                messageRequestDTO.getMessage(),
                LocalDateTime.now());
        Message savedMessage = createMessage(message);

        sendMessageToChatroom(chatroom, savedMessage);
        notificationService.sendNewMessageNotification(chatroom.getId(), message.getMessage());

        return savedMessage;
    }

    private void sendMessageToChatroom(Chatroom chatroom, Message message) {
        rabbitTemplate.convertAndSend(
                RabbitMqBuilder.Name.buildChatroomExchange(chatroom),
                "",
                MessageProcessedDTO.createFromMessage(message)
        );
    }
}
