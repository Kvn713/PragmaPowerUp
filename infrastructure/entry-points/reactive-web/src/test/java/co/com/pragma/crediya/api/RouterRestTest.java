package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.api.mapper.UserDTOMapper;
import co.com.pragma.crediya.api.utils.GlobalExceptionHandler;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;


@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @InjectMocks
    private RouterRest routerRest;

    @MockitoBean
    private Handler handler;

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserDTOMapper userMapper;

    @Mock
    GlobalExceptionHandler globalException;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserTest() {
        Mockito.when(handler.saveUser(any(ServerRequest.class))).thenReturn(ServerResponse.ok().build());
        RouterFunction<ServerResponse> router = routerRest.routerFunction(handler);

        if (router != null) {
            webTestClient.post()
                    .uri("/api/v1/usuarios")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(handler)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(String.class);
        }else {
            webTestClient.post()
                    .uri("/api/v1/usuarios")
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(handler)
                    .exchange()
                    .expectStatus().is5xxServerError()
                    .expectBody(String.class);
        }
    }

    @Test
    void handlerSaveUserTest(){
        CreateUserDTO dto = mock(CreateUserDTO.class);
        User user = mock(User.class);
        ResponseUserDTO responseDTO = mock(ResponseUserDTO.class);
        ServerRequest serverRequest = mock(ServerRequest.class);

        Handler handler1 = new Handler(userUseCase, userMapper, globalException);

        Mockito.when(serverRequest.bodyToMono(CreateUserDTO.class)).thenReturn(Mono.just(dto));
        Mockito.when(globalException.exceptionHandler(dto)).thenReturn(Mono.just(dto));
        Mockito.when(userMapper.toModel(dto)).thenReturn(user);
        //Mockito.when(userUseCase.newUser(user)).thenReturn(Mono.just(user));
        Mockito.when(userUseCase.newUser(user)).thenAnswer(response -> {
            Mono<User> resp = Mono.just(user);
            return resp.map(data -> {
                user.setNombres("John");
                user.setApellidos("Doe");
                user.setCorreoElectronico("correo@email.com");
                return user;
            });
        });
        Mockito.when(userMapper.toResponse(user)).thenReturn(responseDTO);

        Mono<ServerResponse> serverResponse = handler1.saveUser(serverRequest);


        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    /*
    @Test
    void handlerSaveUserTest(){
        User user = mock(User.class);
        UserDTOMapper userDTO = mock(UserDTOMapper.class);
        ResponseUserDTO responseUser = mock(ResponseUserDTO.class);
        GlobalExceptionHandler globalException = mock(GlobalExceptionHandler.class);
        Mockito.when(userUseCase.newUser(any())).thenAnswer(response -> {
            Mono<User> resp = Mono.just(user);
            return resp.map(data -> {
                user.setNombres("John");
                user.setApellidos("Doe");
                user.setCorreoElectronico("correo@email.com");
                return user;
            });
        });

        Mockito.when(userMapper.toResponse(any())).thenReturn(responseUser);
        Handler handler1 = new Handler(userUseCase, userDTO, globalException);

        Mockito.when(globalException.exceptionHandler(any())).thenReturn(Mono.just(Object.class));
        ServerRequest serverRequest = mock(ServerRequest.class);
        Mockito.when(serverRequest.bodyToMono(CreateUserDTO.class)).thenReturn(Mono.just(userMapper.toResponse(user)));

        Mono<ServerResponse> serverResponse = handler1.saveUser(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }*/
}
