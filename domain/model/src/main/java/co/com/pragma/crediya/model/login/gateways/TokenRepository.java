package co.com.pragma.crediya.model.login.gateways;

import co.com.pragma.crediya.model.login.Login;
import reactor.core.publisher.Mono;

public interface TokenRepository {
    Mono<String> generateToken(Login login);
    Mono<Long> expirationTime();
}
