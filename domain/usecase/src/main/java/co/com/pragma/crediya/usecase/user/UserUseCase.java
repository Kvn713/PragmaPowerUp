package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.controlledexceptions.ControlledExceptions;
import co.com.pragma.crediya.model.controlledexceptions.message.ErrorMessage;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> newUser(User user) {
        return userRepository.save(user)
                .onErrorResume(ex -> {
                    if (ex.getMessage().contains("Duplicate entry")){
                        return Mono.error(new ControlledExceptions(ErrorMessage.EXIST_EMAIL));
                    }
                    return Mono.error(ex);
                });
    }

    public Mono<User> getUser(String document) {
        Validations.validate(userRepository.validateDocument(document));
        return userRepository.findByDocument(document)
                .switchIfEmpty(Mono.error(new ControlledExceptions(ErrorMessage.USER_NOT_FOUND)));
    }
}
