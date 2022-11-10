package kiedam.chatapp.backend.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenVerifier extends OncePerRequestFilter {
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(jwtConfig.getAuthHeader());

        if (authHeader == null ||
                authHeader.isEmpty() ||
                !authHeader.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace(
                jwtConfig.getTokenPrefix(),
                ""
        );

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    claimsJws.getBody().getSubject(),
                    null,
                    null
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JwtException ex) {
            throw new IllegalArgumentException(String.format("Token can't be trusted: %s", token));
        }

        filterChain.doFilter(request, response);
    }
}
