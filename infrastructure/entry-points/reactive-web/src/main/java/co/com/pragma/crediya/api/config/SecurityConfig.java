package co.com.pragma.crediya.api.config;

import co.com.pragma.crediya.api.utils.AuthenticationError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    private final ObjectMapper mapper;

    @Bean
    public SecurityWebFilterChain filterChain(
            ServerHttpSecurity httpSecurity,
            JwtFilter filter,
            AuthenticationError errorHandler) {

        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .headers(header -> header
                        .frameOptions(ServerHttpSecurity.HeaderSpec.FrameOptionsSpec::disable))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(errorHandler)
                        .accessDeniedHandler(errorHandler))
                .authorizeExchange(auth -> auth
                        .pathMatchers("/api/v1/login").permitAll()
                        .pathMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/usuarios")
                        .hasAnyRole("ADMIN", "ASESOR")
                        .pathMatchers(HttpMethod.POST, "/api/v1/solicitud")
                        .hasRole("CLIENTE")
                        .pathMatchers(HttpMethod.GET, "/api/v1/consult_by_document")
                        .hasAnyRole("ADMIN", "ASESOR", "CLIENTE")
                        .anyExchange().authenticated())
                .addFilterAfter(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .securityContextRepository(securityContextRepository())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)
                .build();
    }

    public Mono<Claims> getClaims(String token) {
        return Mono.fromCallable(() -> {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
            String newToken = token.replace("Bearer ", "");
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(newToken)
                    .getPayload();
        }).onErrorResume(e -> Mono.error(new BadCredentialsException("Token inválido o expirado", e)));
    }

    private Mono<Authentication> authentication(Authentication authentication) {
        return Mono.just(authentication)
                .flatMap(auth -> getClaims(auth.getCredentials().toString()))
                .map(claim -> {
                    Object rolesObject = claim.get("role");
                    List<GrantedAuthority> authorities = new ArrayList<>();

                    if (rolesObject instanceof String role) {
                        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                        authorities.add(new SimpleGrantedAuthority(authority));
                    }

                    return new UsernamePasswordAuthenticationToken(
                            claim.getSubject(),
                            null,
                            authorities
                    );
                });
    }

    @Bean
    public ServerSecurityContextRepository securityContextRepository() {
        return new ServerSecurityContextRepository() {

            @Override
            public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
                return Mono.empty();
            }

            @Override
            public Mono<SecurityContext> load(ServerWebExchange exchange) {
                String token = exchange.getRequest().getHeaders().getFirst("Authorization");

                return authentication(new UsernamePasswordAuthenticationToken(token, token))
                        .map(auth -> (SecurityContext) new SecurityContextImpl(auth))
                        .onErrorResume(e -> {
                            log.warn("Token invalido: {}", e.getMessage());
                            return Mono.empty();
                        });
            }
        };
    }

}
