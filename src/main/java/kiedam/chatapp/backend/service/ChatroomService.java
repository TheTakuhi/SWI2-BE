package kiedam.chatapp.backend.service;

import kiedam.chatapp.backend.builder.RabbitMqBuilder;
import kiedam.chatapp.backend.model.Chatroom;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.repository.ChatRoomRepository;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChatroomService {
    private final ChatRoomRepository chatRoomRepository;
    private final AmqpAdmin amqpAdmin;
    private final UserService userService;
    private final AmqpService amqpService;

    @Autowired
    public ChatroomService(ChatRoomRepository chatRoomRepository,
                           UserService userService,
                           AmqpAdmin amqpAdmin,
                           AmqpService amqpService) {
        this.chatRoomRepository = chatRoomRepository;
        this.userService = userService;
        this.amqpAdmin = amqpAdmin;
        this.amqpService = amqpService;
    }

    public List<Chatroom> getAllChatrooms() {
        User loggedUser = userService.getLoggedUser();
        return loggedUser.getChatrooms();
    }

    public Chatroom getChatroomById(Long chatRoomId) {
        return chatRoomRepository
                .findChatRoomById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException(String.format("ChatRoom with id %d not found.", chatRoomId)));
    }

    public Chatroom createNewChatroom(Set<Long> userIds, String title) {
        Set<User> attachedUsers = new HashSet<>(
                userService.getUsersFiltered(user -> userIds.contains(user.getId()))
        );
        Chatroom existingChatroom = _getChatroomIfExists(userIds);
        if (existingChatroom == null) {
            Chatroom chatroom = new Chatroom(attachedUsers, title);
            Chatroom savedChatroom = chatRoomRepository.save(chatroom);
            _createBindingsForChatroom(savedChatroom);
            return savedChatroom;
        } else {
            return existingChatroom;
        }
    }

    public void createDemoGroupChatroom(Set<Long> userIds, String title){
        Set<User> attachedUsers = new HashSet<>(
                userService.getUsersFiltered(user -> userIds.contains(user.getId()))
        );
        Chatroom chatroom = new Chatroom(attachedUsers, title);
        Chatroom savedChatroom = chatRoomRepository.save(chatroom);
        _createBindingsForChatroom(savedChatroom);
    }

    public Chatroom createNewChatroom(Set<Long> userIds, Long moderatorId, String title) {
        if (moderatorId == null)
            moderatorId = userService.getLoggedUser().getId();
        userIds.add(moderatorId);
        return createNewChatroom(userIds, title);
    }


    private void _createBindingsForChatroom(Chatroom chatroom) {
        String exchangeName = RabbitMqBuilder.Name.buildChatroomExchange(chatroom);
        amqpService.declareExchange(() -> new FanoutExchange(exchangeName));
        chatroom.getUsers()
                .forEach(user -> {
                    String queueName = amqpService.declareQueue(RabbitMqBuilder.Name.buildChatroomUserQueue(user, chatroom));
                    amqpAdmin.declareBinding(
                            new Binding(
                                    queueName,
                                    Binding.DestinationType.QUEUE,
                                    exchangeName,
                                    "",
                                    null)
                    );
                });
    }

    private Chatroom _getChatroomIfExists(Set<Long> userIds) {
        User loggedUser = userService.getLoggedUser();
        boolean exists = false;
        Chatroom existingChatroom = null;
        for (Chatroom ch : loggedUser.getChatrooms()) {
            if (exists) break;
            Set<Long> currentIds = ch.getUsers()
                    .stream()
                    .map(User::getId)
                    .collect(Collectors.toSet());
            exists = _compareAttachedIds(currentIds, userIds);
            if (exists) existingChatroom = ch;
        }
        return existingChatroom;
    }

    private boolean _compareAttachedIds(Set<Long> array1, Set<Long> array2) {
        return array1.equals(array2);
    }
}
