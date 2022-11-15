package kiedam.chatapp.backend.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CredentialsAuthRequest {
    private String username;
    private String password;
}
