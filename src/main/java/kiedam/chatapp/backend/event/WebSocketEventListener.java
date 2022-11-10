package kiedam.chatapp.backend.event;

import kiedam.chatapp.backend.builder.RabbitMqBuilder;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpAttributesContextHolder;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.List;
import java.util.Set;

public class WebSocketEventListener {
    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpleMessageListenerContainer container;
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public WebSocketEventListener(SimpleMessageListenerContainer container,
                                      UserService userService,
                                      SubscriptionService subscriptionService) {
        this.container = container;
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        User user = _extractUserFromEvent(event);
        String sessionId = getSessionId();

        if (user == null) return;

        log.info(user.getUsername() + " has connected with session \"" + sessionId + "\"");
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        User user = _extractUserFromEvent(event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = getSessionId();

        if (user == null) return;

        Long chatroomId = _extractChatroomIdFromHeaders(headerAccessor);

        if (chatroomId != null) {
            String queueName = RabbitMqBuilder.Name.buildChatroomUserQueue(user.getId(), chatroomId);
            container.addQueueNames(queueName);
            subscriptionService.addQueue(sessionId, queueName);
            log.info(String.format("%s is subscribed to chatroom \"%d\" to queue \"%s\"", user.getUsername(), chatroomId, queueName));
        }else{
            log.info(String.format("%s is listening notifications", user.getUsername()));
        }
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        User user = _extractUserFromEvent(event);
        if (user == null) return;

        String sessionId = getSessionId();

        Set<String> queueNames = subscriptionService.getSubscribedQueues(sessionId);
        subscriptionService.removeAllQueues(sessionId);

        String[] queues = new String[queueNames.size()];
        container.removeQueueNames(queueNames.toArray(queues));

        queueNames.forEach(name -> log.info(user.getUsername() + " has unsubscribed from queue \"" + name + "\""));
        log.info(user.getUsername() + " has disconnected from session \"" + sessionId + "\"");
    }


    private Long _extractChatroomIdFromHeaders(StompHeaderAccessor accessor) {
        List<String> chatroomIdList = accessor.getNativeHeader("chatroomId");
        if (chatroomIdList == null) return null;

        String chatRoomIdS = chatroomIdList.get(0);
        Long chatRoomId = null;
        try {
            chatRoomId = Long.parseLong(chatRoomIdS);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return chatRoomId;
    }

    private User _extractUserFromEvent(AbstractSubProtocolEvent event) {
        if (event.getUser() == null) return null;

        String username = event.getUser().getName();
        if (username == null || username.isBlank()) return null;

        return (User) userService.loadUserByUsername(username);
    }

    private String getSessionId() {
        return SimpAttributesContextHolder.currentAttributes().getSessionId();
    }
}
