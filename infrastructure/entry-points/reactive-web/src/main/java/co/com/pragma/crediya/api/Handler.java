package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.mapper.UserDTOMapper;
import co.com.pragma.crediya.api.utils.GlobalExceptionHandler;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userMapper;
    private final GlobalExceptionHandler exceptionHandler;

    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        log.trace("New request");
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(data -> log.info("Obtain request data", data))
                .flatMap(exceptionHandler::exceptionHandler)
                .flatMap(request -> userUseCase.newUser(userMapper.toModel(request)))
                .doOnSuccess(next -> log.info("Save new user", next))
                .flatMap(newUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userMapper.toResponse(newUser))
                )
                .doOnError(error -> log.error("Failed process dont save new user", error))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(error.getMessage()));
    }

}
