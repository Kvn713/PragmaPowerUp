package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.request.CreateUserDTO;
import co.com.pragma.crediya.api.dto.request.LoginRequestDTO;
import co.com.pragma.crediya.api.mapper.LoginDTOMapper;
import co.com.pragma.crediya.api.mapper.UserDTOMapper;
import co.com.pragma.crediya.api.utils.RequestValidator;
import co.com.pragma.crediya.usecase.login.LoginUseCase;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final UserUseCase userUseCase;
    private final UserDTOMapper userMapper;
    private final RequestValidator exceptionHandler;
    private final LoginUseCase loginUseCase;
    private final LoginDTOMapper loginMapper;

    @PreAuthorize("hasAnyRole('ADMIN','ASESOR')")
    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        log.trace("New request");
        return serverRequest.bodyToMono(CreateUserDTO.class)
                .doOnNext(data -> log.info("Obtain request data: {}", data))
                .flatMap(exceptionHandler::exceptionHandler)
                .flatMap(request -> userUseCase.newUser(userMapper.toModel(request)))
                .doOnSuccess(next -> log.info("Saved new user: {}", next))
                .flatMap(newUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(userMapper.toResponse(newUser)))
                .doOnError(error -> log.error("Failed process, don't save new user: ", error))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(error.getMessage()));
    }

    @PreAuthorize("hasAnyRole('ADMIN','ASESOR', 'CLIENTE')")
    public Mono<ServerResponse> getByDocument(ServerRequest serverRequest) {
        String document = serverRequest.queryParam("document")
                .orElseThrow(() -> new IllegalArgumentException("Param document is not present"));
        //String document = serverRequest.queryParams().toSingleValueMap().get("document");
        return userUseCase.getUser(document)
                .flatMap(response -> {
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(response);
                })
                .doOnError(error -> log.error("Failed process, not found user: ", error))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(error.getMessage()));
    }


    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginRequestDTO.class)
                .flatMap(exceptionHandler::exceptionHandler)
                .flatMap(request -> loginUseCase.login(loginMapper.toModel(request)))
                .flatMap(response ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(loginMapper.toResponse(response)))
                .doOnError(error -> log.error("Login Failed: ", error))
                .onErrorResume(error -> ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(error.getMessage()));
    }


}
