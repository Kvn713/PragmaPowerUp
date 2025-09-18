package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.request.CreateUserDTO;
import co.com.pragma.crediya.api.dto.request.LoginRequestDTO;
import co.com.pragma.crediya.api.dto.response.ResponseLoginDTO;
import co.com.pragma.crediya.api.dto.response.ResponseUserDTO;
import co.com.pragma.crediya.api.dto.response.SignUpDTO;
import co.com.pragma.crediya.api.mapper.LoginDTOMapper;
import co.com.pragma.crediya.api.mapper.UserDTOMapper;
import co.com.pragma.crediya.api.utils.RequestValidator;
import co.com.pragma.crediya.model.controlledexceptions.ControlledExceptions;
import co.com.pragma.crediya.model.controlledexceptions.message.ErrorMessage;
import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.usecase.login.LoginResponse;
import co.com.pragma.crediya.usecase.login.LoginUseCase;
import co.com.pragma.crediya.usecase.user.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@AutoConfigureWebTestClient
@ExtendWith(MockitoExtension.class)
//@WithMockUser // Simula un usuario autenticado
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
    private RequestValidator globalException;

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private LoginDTOMapper loginMapper;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
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
                    .expectStatus().is4xxClientError()
                    .expectBody(String.class);
        } else {
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
    void loginTest() {
        // Arrange
        UUID id = UUID.randomUUID();
        LoginRequestDTO requestDTO = new LoginRequestDTO(id, "user@email.com", "password123", "admin", new ArrayList<>());
        Login loginModel = new Login(id, "user@email.com", "password123", "admin", new ArrayList<>());
        SignUpDTO userDto = new SignUpDTO("1234567890", "user@email.com", "admin", new ArrayList<>());
        ResponseLoginDTO responseDTO = new ResponseLoginDTO("jwt-token", "content-type", 1L, userDto);
        LoginResponse loginResponse = new LoginResponse("jwt-token", 1L, loginModel);

        ServerRequest serverRequest = MockServerRequest
                .builder()
                .body(Mono.just(requestDTO));

        //Mockito.when(exceptionHandler.exceptionHandler(any(Mono.class))).thenAnswer(invocation -> invocation.getArgument(0));
//        Mockito.when(loginMapper.toModel(requestDTO)).thenReturn(loginModel);
//        Mockito.when(loginUseCase.login(loginModel)).thenReturn(Mono.just(loginResponse));
//        Mockito.when(loginMapper.toResponse(loginModel)).thenReturn(userDto);


        when(handler.login(any(ServerRequest.class))).thenReturn(ServerResponse.ok().build());

        Mono<ServerResponse> responseMono = handler.login(serverRequest);

        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.statusCode().is2xxSuccessful()).expectNext();
    }

    @Test
    void handlerSaveUserTest() {
        CreateUserDTO dto = mock(CreateUserDTO.class);
        User user = mock(User.class);
        ResponseUserDTO responseDTO = mock(ResponseUserDTO.class);
        ServerRequest serverRequest = mock(ServerRequest.class);

        Handler handler1 = new Handler(userUseCase, userMapper, globalException, null, null);

        when(serverRequest.bodyToMono(CreateUserDTO.class)).thenReturn(Mono.just(dto));
        when(globalException.exceptionHandler(dto)).thenReturn(Mono.just(dto));
        when(userMapper.toModel(dto)).thenReturn(user);
        //when(userUseCase.newUser(user)).thenReturn(Mono.just(user));
        when(userUseCase.newUser(user)).thenAnswer(response -> {
            Mono<User> resp = Mono.just(user);
            return resp.map(data -> {
                user.setName("John");
                user.setLastName("Doe");
                user.setEmail("correo@email.com");
                return user;
            });
        });
        when(userMapper.toResponse(user)).thenReturn(responseDTO);

        Mono<ServerResponse> serverResponse = handler1.saveUser(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void handlerSaveUserTestError() {
        CreateUserDTO dto = mock(CreateUserDTO.class);
        User user = mock(User.class);
        ServerRequest serverRequest = mock(ServerRequest.class);

        Handler handler1 = new Handler(userUseCase, userMapper, globalException, null, null);

        when(serverRequest.bodyToMono(CreateUserDTO.class)).thenReturn(Mono.just(dto));
        when(globalException.exceptionHandler(dto)).thenReturn(Mono.just(dto));
        when(userMapper.toModel(dto)).thenReturn(user);
        when(userUseCase.newUser(user)).thenReturn(Mono.error(new ControlledExceptions(ErrorMessage.DEFAULT_EXCEPTION)));

        Mono<ServerResponse> serverResponse = handler1.saveUser(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void handlerGetByDocumentTest() {
        User user = mock(User.class);
        ServerRequest serverRequest = mock(ServerRequest.class);
        Handler handler1 = new Handler(userUseCase, userMapper, globalException, null, null);

        when(serverRequest.queryParam("document")).thenReturn("123456".describeConstable());
        when(userUseCase.getUser(any())).thenReturn(Mono.just(user));

        Mono<ServerResponse> serverResponse = handler1.getByDocument(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void handlerGetByDocumentTestError() {
        User user = mock(User.class);
        ServerRequest serverRequest = mock(ServerRequest.class);
        Handler handler1 = new Handler(userUseCase, userMapper, globalException, null, null);

        when(serverRequest.queryParam("document")).thenReturn("123456".describeConstable());
        when(userUseCase.getUser(any())).thenReturn(Mono.error(new ControlledExceptions(ErrorMessage.USER_NOT_FOUND)));

        Mono<ServerResponse> serverResponse = handler1.getByDocument(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void handlerLoginTest() {
        User user = mock(User.class);
        Login login = mock(Login.class);
        LoginResponse loginResponse = mock(LoginResponse.class);
        ServerRequest serverRequest = mock(ServerRequest.class);
        LoginRequestDTO loginRequestDTO = mock(LoginRequestDTO.class);
        ResponseLoginDTO responseLoginDTO = mock(ResponseLoginDTO.class);

        Handler handler1 = new Handler(null, null, globalException, loginUseCase, loginMapper);

        when(serverRequest.bodyToMono(LoginRequestDTO.class)).thenReturn(Mono.just(loginRequestDTO));
        when(globalException.exceptionHandler(loginRequestDTO)).thenReturn(Mono.just(loginRequestDTO));
        when(loginMapper.toModel(loginRequestDTO)).thenReturn(login);
        when(loginUseCase.login(login)).thenReturn(Mono.just(loginResponse));
        when(loginMapper.toResponse(loginResponse)).thenReturn(responseLoginDTO);

        Mono<ServerResponse> serverResponse = handler1.login(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void handlerLoginTestError() {
        User user = mock(User.class);
        Login login = mock(Login.class);
        LoginResponse loginResponse = mock(LoginResponse.class);
        ServerRequest serverRequest = mock(ServerRequest.class);
        LoginRequestDTO loginRequestDTO = mock(LoginRequestDTO.class);
        ResponseLoginDTO responseLoginDTO = mock(ResponseLoginDTO.class);

        Handler handler1 = new Handler(null, null, globalException, loginUseCase, loginMapper);

        when(serverRequest.bodyToMono(LoginRequestDTO.class)).thenReturn(Mono.just(loginRequestDTO));
        when(globalException.exceptionHandler(loginRequestDTO)).thenReturn(Mono.just(loginRequestDTO));
        when(loginMapper.toModel(loginRequestDTO)).thenReturn(login);
        when(loginUseCase.login(login)).thenReturn(Mono.error(new ControlledExceptions(ErrorMessage.BAD_CREDENTIALS)));

        Mono<ServerResponse> serverResponse = handler1.login(serverRequest);

        StepVerifier.create(serverResponse)
                .expectNextMatches(resp -> resp.statusCode().is4xxClientError())
                .verifyComplete();
    }
}
