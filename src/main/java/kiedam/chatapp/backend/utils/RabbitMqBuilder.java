package kiedam.chatapp.backend.utils;

import kiedam.chatapp.backend.model.Chatroom;
import kiedam.chatapp.backend.model.User;

public class RabbitMqBuilder {

    public static class Name {
        public static String buildChatroomExchange(Chatroom chatroom) {
            return String.format("chatroom.%d.exchange", chatroom.getId());
        }

        public static String buildChatroomUserQueue(Long userId, Long chatroomId) {
            return String.format("user_%d.chatroom_%d.queue", userId, chatroomId );
        }

        public static String buildChatroomUserQueue(User user, Chatroom chatroom) {
            return buildChatroomUserQueue(user.getId(), chatroom.getId());
        }
    }
}
