package kiedam.chatapp.backend.mapper;

import kiedam.chatapp.backend.dto.MessageResponseDTO;
import kiedam.chatapp.backend.model.Message;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Message, MessageResponseDTO>() {
            protected void configure() {
                map().setSenderUsername(source.getSender().getUsername());
            }
        });
        return modelMapper;
    }
}
