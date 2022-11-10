package kiedam.chatapp.backend;

import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.repository.ChatRoomRepository;
import kiedam.chatapp.backend.service.ChatroomService;
import kiedam.chatapp.backend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BackendApplication{
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
    @Bean
    CommandLineRunner commandLineRunner(
            UserService userService,
            ChatroomService chatroomService,
            PasswordEncoder passwordEncoder) {
        return args -> {
            List<User> users = List.of(
                    new User("damek", passwordEncoder.encode("heslo")),
                    new User("kiedron", passwordEncoder.encode("heslo")),
                    new User("chlopcik", passwordEncoder.encode("heslo")),
                    new User("sikora", passwordEncoder.encode("heslo")),
                    new User("bobos", passwordEncoder.encode("heslo"))
            );
            users.forEach(userService::createUser);

            chatroomService.createDemoGroupChatroom(
                    Set.of(1L, 2L, 3L),
                    "School group"
            );
        };
    }
}
