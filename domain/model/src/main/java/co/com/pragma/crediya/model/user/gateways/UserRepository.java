package co.com.pragma.crediya.model.user.gateways;

import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UserRepository {

    Mono<User> save(User User);

    Mono<Boolean> validateDocument(String document);

    Mono<User> findByDocument(String document);

}
