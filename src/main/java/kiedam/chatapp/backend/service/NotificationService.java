package kiedam.chatapp.backend.service;

import kiedam.chatapp.backend.builder.StompBuilder;
import kiedam.chatapp.backend.model.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ChatroomService chatroomService;

    public NotificationService(SimpMessagingTemplate messagingTemplate,
                               UserService userService,
                               ChatroomService chatroomService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.chatroomService = chatroomService;
    }

    public void sendJustWritingInChatroom(Long chatroomId) {
        User loggedUser = userService.getLoggedUser();
        Notification notification = Notification.createNew(loggedUser, NotificationType.JUST_WRITING);
        Chatroom chatroom = chatroomService.getChatroomById(chatroomId);
        chatroom.getUsers().forEach(user ->
                messagingTemplate.convertAndSend(StompBuilder.buildChatroomChannel(user.getId(), chatroomId), notification)
        );
    }

    public void sendCanceledWritingInChatroom(Long chatroomId) {
        User loggedUser = userService.getLoggedUser();
        Notification notification = Notification.createNew(loggedUser, NotificationType.CANCELED_WRITING);
        Chatroom chatroom = chatroomService.getChatroomById(chatroomId);
        chatroom.getUsers().forEach(user ->
                messagingTemplate.convertAndSend(StompBuilder.buildChatroomChannel(user.getId(), chatroomId), notification)
        );
    }

    public void sendNewMessageNotification(Long chatroomId, String message) {
        User loggedUser = userService.getLoggedUser();
        Chatroom chatroom = chatroomService.getChatroomById(chatroomId);
        Notification notification = MessageNotification.createNew(message, loggedUser, NotificationType.NEW_MESSAGE, chatroom);
        chatroom.getUsers()
                .stream()
                .filter(u -> !u.getId().equals(loggedUser.getId()))
                .forEach(user ->
                        messagingTemplate.convertAndSend(StompBuilder.buildNotificationChannel(user.getId()), notification)
                );
    }
}
