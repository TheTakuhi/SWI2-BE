package kiedam.chatapp.backend.service;

import kiedam.chatapp.backend.model.User;
import kiedam.chatapp.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsers(String searchByTerm) {
        return getUsersFiltered(u -> u.getUsername().contains(searchByTerm));
    }

    public List<User> getUsersFiltered(Predicate<? super User> predicate) {
        return userRepository
                .findAll()
                .stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));
    }

    public User getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        return (User) loadUserByUsername(username);
    }
}
