package co.com.pragma.crediya.model.login.gateways;

import co.com.pragma.crediya.model.login.Login;
import reactor.core.publisher.Mono;

public interface LoginRepository {

    Mono<Login> findByEmail(Login login);

}
