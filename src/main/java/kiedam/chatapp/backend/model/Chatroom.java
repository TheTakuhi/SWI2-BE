package kiedam.chatapp.backend.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Chatroom")
@Table(name = "chatroom")
public class Chatroom implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "chatroom_user",
            joinColumns = {@JoinColumn(name = "chatroom_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> users = new HashSet<>();

    private String title;

    public Chatroom(Set<User> users) {
        this.users = users;
    }

    public Chatroom(Set<User> users, String title) {
        this.users = users;
        this.title = title;
    }
}
