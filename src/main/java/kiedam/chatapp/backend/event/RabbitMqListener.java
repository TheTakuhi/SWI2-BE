package kiedam.chatapp.backend.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import kiedam.chatapp.backend.builder.StompBuilder;
import kiedam.chatapp.backend.dto.MessageProcessedDTO;
import kiedam.chatapp.backend.dto.MessageResponseDTO;
import kiedam.chatapp.backend.mapper.MessageMapper;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RabbitMqListener implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqListener.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final MessageMapper messageMapper;

    public RabbitMqListener(SimpMessagingTemplate messagingTemplate,
                            @Lazy UserService userService,
                            ObjectMapper objectMapper,
                            MessageMapper messageMapper) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.objectMapper = objectMapper;
        this.messageMapper = messageMapper;
    }

    @Override
    public void onMessage(Message messageRaw) {
        try {
            MessageProcessedDTO messageProcessedDTO = objectMapper.readValue(messageRaw.getBody(), MessageProcessedDTO.class);

            log.info("Received = " + messageProcessedDTO);

            String queue = messageRaw.getMessageProperties().getConsumerQueue();

            User user = userService.getUserById(extractUserId(queue));
            MessageResponseDTO responseDTO = messageMapper.mapToMessageResponseDTO(messageProcessedDTO, user);

            messagingTemplate.convertAndSend(
                    StompBuilder.buildChatroomChannel(user.getId(), responseDTO.getChatRoomId()),
                    responseDTO);
        } catch (IOException ex) {
            log.error("JsonMessageResponseDTO to MessageResponseDto mapping operation failed.");
            log.error(ex.getMessage(), ex);
        }
    }

    private Long extractUserId(String queueName) {
        String id = queueName.split("\\.")[0];
        id = id.substring(id.indexOf("_") + 1);
        return Long.parseLong(id);
    }
}
