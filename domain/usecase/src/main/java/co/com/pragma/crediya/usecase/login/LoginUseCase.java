package co.com.pragma.crediya.usecase.login;

import co.com.pragma.crediya.model.controlledexceptions.ControlledExceptions;
import co.com.pragma.crediya.model.controlledexceptions.message.ErrorMessage;
import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.login.gateways.EncodeCredentials;
import co.com.pragma.crediya.model.login.gateways.LoginRepository;
import co.com.pragma.crediya.model.login.gateways.TokenRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
public class LoginUseCase {
    private final LoginRepository loginRepository;
    private final EncodeCredentials encodeCredentials;
    private final TokenRepository tokenRepository;

    public Mono<LoginResponse> login(Login login) {
        login.setId(UUID.randomUUID());
        return loginRepository.findByEmail(login)
                .switchIfEmpty(Mono.error(new ControlledExceptions(ErrorMessage.BAD_CREDENTIALS)))
                .flatMap(user -> encodeCredentials.matches(login.getPassword(), user.getPassword())
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new ControlledExceptions(ErrorMessage.BAD_CREDENTIALS)))
                        .flatMap(response -> tokenRepository.generateToken(user)
                                .flatMap(token ->
                                        tokenRepository.expirationTime()
                                                .map(expiration ->
                                                        new LoginResponse(
                                                                token,
                                                                expiration,
                                                                user
                                                        )
                                                )
                                )
                        )
                );
    }

}
