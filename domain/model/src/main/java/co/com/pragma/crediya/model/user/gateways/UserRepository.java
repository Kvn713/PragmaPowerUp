package co.com.pragma.crediya.model.user.gateways;

import co.com.pragma.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

public interface UserRepository {

    Mono<User> save(User User);
    Flux<User> finAll();
    Mono<User> findById(BigInteger id);
    Mono<User> edit(User User);
    Mono<Void> deleteById(BigInteger id);

}
