package co.com.pragma.crediya.r2dbc.security;

import co.com.pragma.crediya.model.controlledexceptions.ControlledExceptions;
import co.com.pragma.crediya.model.controlledexceptions.message.ErrorMessage;
import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.login.gateways.LoginRepository;
import co.com.pragma.crediya.r2dbc.repository.RoleReactiveRepository;
import co.com.pragma.crediya.r2dbc.repository.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoginReactiveRepositoryAdapter implements LoginRepository {

    private final UserReactiveRepository userReactiveRepository;
    private final RoleReactiveRepository roleReactiveRepository;

    @Override
    public Mono<Login> findByEmail(Login login) {
        return userReactiveRepository.getByEmail(login.getEmail())
                .flatMap(user -> roleReactiveRepository.findById(user.getRoleId())
                        .switchIfEmpty(Mono.error(new ControlledExceptions(ErrorMessage.UNAUTHORIZED_ROLE)))
                        .map(role -> Login.builder()
                                .id(login.getId())
                                .email(user.getEmail())
                                .password(user.getPassword())
                                .role(role.getName())
                                .permissions(List.of())
                                .build()

                        )
                );
    }
}
