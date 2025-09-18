package co.com.pragma.crediya.usecase.user;

import reactor.core.publisher.Mono;


public class Validations {

    public static Mono<Void> validate(Mono<Boolean> exist) {
        return exist.flatMap(response -> {
            if (!response) {
                return Mono.error(new IllegalArgumentException("Not Found user in database"));
            }
            return Mono.empty(); // Devuelve Mono<Void> si todo está bien
        });
    }

}
