package kiedam.chatapp.backend.builder;

import kiedam.chatapp.backend.model.Chatroom;
import kiedam.chatapp.backend.model.User;

public class StompBuilder {
    public static String buildChatroomChannel(Long userId, Long chatroomId) {
        return String.format("/topic/user_%d/chatroom_%d", userId, chatroomId);
    }

    public static String buildChatroomChannel(User user, Chatroom chatroom) {
        return buildChatroomChannel(user.getId(), chatroom.getId());
    }

    public static String buildNotificationChannel(Long userId) {
        return String.format("/topic/user_%d/notifications", userId);
    }
}
