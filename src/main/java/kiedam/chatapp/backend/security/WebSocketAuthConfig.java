package kiedam.chatapp.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import kiedam.chatapp.backend.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public WebSocketAuthConfig(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                        List<String> authorization = accessor.getNativeHeader(jwtConfig.getAuthHeader());
                        String token = authorization.get(0).replace(
                                jwtConfig.getTokenPrefix(),
                                ""
                        );
                        Jws<Claims> claimsJws = Jwts.parser()
                                .setSigningKey(secretKey)
                                .parseClaimsJws(token);

                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                claimsJws.getBody().getSubject(),
                                null,
                                null
                        );
                        accessor.setUser(auth);
                        if (SecurityContextHolder.getContext().getAuthentication() == null)
                            SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
                return message;
            }
        });
    }
}
