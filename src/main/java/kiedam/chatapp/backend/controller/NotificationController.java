package kiedam.chatapp.backend.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import kiedam.chatapp.backend.service.NotificationService;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @MessageMapping("/chatrooms/{chatroomId}/writing")
    public void justWriting(@DestinationVariable("chatroomId") Long chatroomId) {
        notificationService.sendJustWritingInChatroom(chatroomId);
    }

    @MessageMapping("/chatrooms/{chatroomId}/writing_canceled")
    public void canceledWriting(@PathVariable("chatroomId") Long chatroomId) {
        notificationService.sendCanceledWritingInChatroom(chatroomId);
    }
}
