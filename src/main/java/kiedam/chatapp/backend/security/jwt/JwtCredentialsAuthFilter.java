package kiedam.chatapp.backend.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtCredentialsAuthFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JwtCredentialsAuthFilter(AuthenticationManager authManager,
                                    JwtConfig jwtConfig,
                                    SecretKey secretKey) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            CredentialsAuthRequest authRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), CredentialsAuthRequest.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword(),
                    null
            );

            return authManager.authenticate(auth);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .setIssuedAt(new Date())
                .setExpiration(
                        java.sql.Date.valueOf(
                                LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())
                        ))
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfig.getAuthHeader(), jwtConfig.getTokenPrefix() + token);
    }
}
