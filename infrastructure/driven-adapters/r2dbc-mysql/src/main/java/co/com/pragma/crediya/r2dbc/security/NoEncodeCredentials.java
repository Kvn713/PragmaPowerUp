package co.com.pragma.crediya.r2dbc.security;

import co.com.pragma.crediya.model.login.gateways.EncodeCredentials;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@Profile({"dev", "local"})
public class NoEncodeCredentials implements EncodeCredentials {

    @Override
    public Mono<String> encode(String password) {
        return Mono.just(password);
    }

    @Override
    public Mono<Boolean> matches(String password, String encodePassword) {
        return Mono.just(Objects.equals(password, encodePassword));
    }
}
