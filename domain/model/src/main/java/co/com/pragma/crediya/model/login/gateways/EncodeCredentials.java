package co.com.pragma.crediya.model.login.gateways;

import reactor.core.publisher.Mono;

public interface EncodeCredentials {

    Mono<String> encode(String password);

    Mono<Boolean> matches(String password, String encodePassword);
}
