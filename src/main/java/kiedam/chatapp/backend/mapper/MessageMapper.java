package kiedam.chatapp.backend.mapper;

import kiedam.chatapp.backend.dto.MessageProcessedDTO;
import kiedam.chatapp.backend.dto.MessageResponseDTO;
import kiedam.chatapp.backend.model.Message;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {
    private final ModelMapper modelMapper;
    private final UserService userService;

    public MessageMapper(ModelMapper modelMapper,
                         UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    public MessageResponseDTO mapToMessageResponseDTO(MessageProcessedDTO message, User user) {
        MessageResponseDTO messageResponseDTO = modelMapper.map(message, MessageResponseDTO.class);
        messageResponseDTO.setOwned(user.getId().equals(messageResponseDTO.getSenderId()));
        return messageResponseDTO;
    }

    public MessageResponseDTO mapToMessageResponseDTO(MessageProcessedDTO message) {
        User user = userService.getLoggedUser();
        return mapToMessageResponseDTO(message, user);
    }

    public MessageResponseDTO mapToMessageResponseDTO(Message message) {
        User user = userService.getLoggedUser();
        MessageResponseDTO messageResponseDTO = MessageResponseDTO.createFromMessage(message);
        messageResponseDTO.setOwned(user.getId().equals(messageResponseDTO.getSenderId()));
        return messageResponseDTO;
    }
}
