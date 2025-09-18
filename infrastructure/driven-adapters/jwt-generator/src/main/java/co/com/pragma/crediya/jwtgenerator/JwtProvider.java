package co.com.pragma.crediya.jwtgenerator;

import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.login.gateways.TokenRepository;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider implements TokenRepository {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;
    @Value("${jwt.issuer}")
    private String issuer;

    @Override
    public Mono<String> generateToken(Login login) {
        return Mono.fromCallable(() -> Jwts.builder()
                        .subject(login.getEmail())
                        .issuer(issuer)
                        .claim("permissions", login.getPermissions())
                        .claim("role", login.getRole() != null ? login.getRole() : "CLIENTE")
                        .issuedAt(new Date())
                        .expiration(new Date(new Date().getTime() + expiration * 1000L))
                        .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                        .compact()
                ).subscribeOn(Schedulers.boundedElastic())
                .onErrorResume(e -> Mono.error(new JwtException("Token Generate Failed!")));
    }

    @Override
    public Mono<Long> expirationTime() {
        return Mono.just(expiration);
    }
}
