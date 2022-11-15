package kiedam.chatapp.backend.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(
        name = "\"user\"",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "user_username_unique",
                        columnNames = "username"
                )
        })
public class User implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @Column(
            name = "username",
            nullable = false
    )
    private String username;

    @Column(
            name = "password",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String password;

    @ManyToMany(
            mappedBy = "users"
    )
    private List<Chatroom> chatrooms;

    @Column(
            name = "account_non_expired",
            nullable = false
    )
    private boolean isAccountNonExpired;

    @Column(
            name = "non_locked",
            nullable = false
    )
    private boolean isAccountNonLocked;

    @Column(
            name = "credentials_non_expired",
            nullable = false
    )
    private boolean isCredentialsNonExpired;

    @Column(
            name = "enabled",
            nullable = false
    )
    private boolean isEnabled;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.chatrooms = new ArrayList<>();
        this.isAccountNonExpired = true;
        this.isAccountNonLocked = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
