package co.com.pragma.crediya.r2dbc.repository;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, String>, ReactiveQueryByExampleExecutor<UserEntity> {

    Mono<User> getByDocument(String document);
    Mono<User> getByEmail(String email);
    Mono<Boolean> document(String document);


}
