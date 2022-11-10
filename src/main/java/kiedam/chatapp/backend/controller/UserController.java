package kiedam.chatapp.backend.controller;

import kiedam.chatapp.backend.dto.UserDTO;
import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper mapper;


    @Autowired
    public UserController(UserService userService,
                          ModelMapper modelMapper) {
        this.userService = userService;
        this.mapper = modelMapper;
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/detail")
    public UserDTO getUserDetail() {
        User user = userService.getLoggedUser();
        return mapper.map(user, UserDTO.class);
    }

    @GetMapping("/search")
    public List<UserDTO> getUsersBySearch(@RequestParam("term") String searchTerm) {
        return userService.getAllUsers(searchTerm)
                .stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
}
