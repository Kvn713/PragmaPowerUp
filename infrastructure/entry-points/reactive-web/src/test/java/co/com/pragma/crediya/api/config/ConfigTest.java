package co.com.pragma.crediya.api.config;

import co.com.pragma.crediya.api.Handler;
import co.com.pragma.crediya.api.RouterRest;
import co.com.pragma.crediya.api.dto.request.LoginRequestDTO;
import co.com.pragma.crediya.api.dto.response.ResponseLoginDTO;
import co.com.pragma.crediya.api.dto.response.SignUpDTO;
import co.com.pragma.crediya.api.mapper.LoginDTOMapper;
import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.login.gateways.EncodeCredentials;
import co.com.pragma.crediya.model.login.gateways.TokenRepository;
import co.com.pragma.crediya.usecase.login.LoginResponse;
import co.com.pragma.crediya.usecase.login.LoginUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
@ExtendWith(MockitoExtension.class)
class ConfigTest {

    @InjectMocks
    private RouterRest routerRest;

    @MockitoBean
    private Handler handler;

    @Mock
    private LoginUseCase loginUseCase;

    @Autowired
    private WebTestClient webTestClient;

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void corsConfigTest() {
        String allowedOrigins = "http://localhost:8080,http://localhost:8081";

        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter(allowedOrigins);

        assertNotNull(corsWebFilter);
        assertEquals(CorsWebFilter.class, corsWebFilter.getClass());
    }

    /*

    @Test
    void corsConfigurationShouldAllowOrigins() {
        UUID id = UUID.randomUUID();
        LoginDTOMapper loginMapper = mock(LoginDTOMapper.class);
        LoginRequestDTO requestDTO = new LoginRequestDTO(id, "user@email.com", "password123", "admin", new ArrayList<>());
        Login loginModel = new Login(id, "user@email.com", "password123", "admin", new ArrayList<>());
        SignUpDTO userDto = new SignUpDTO("1234567890", "user@email.com", "admin", new ArrayList<>());
        ResponseLoginDTO responseDTO = new ResponseLoginDTO("jwt-token", "content-type", 1L, userDto);
        LoginResponse loginResponse = new LoginResponse("jwt-token", 1L, loginModel);

        //Mockito.when(loginUseCase.login(any())).thenReturn(mock(LoginResponse.class));
               Mockito.when(loginMapper.toModel(requestDTO)).thenReturn(loginModel);
        Mockito.when(loginUseCase.login(loginModel)).thenReturn(Mono.just(loginResponse));
        Mockito.when(loginMapper.toResponse(loginModel)).thenReturn(userDto);
        ServerRequest serverRequest = MockServerRequest
                .builder()
                .body(Mono.just(requestDTO));

        Mockito.when(handler.login(any(ServerRequest.class))).thenReturn(ServerResponse.ok().build());
        //Mono<ServerResponse> responseMono = handler.login(serverRequest);

        handler.login(serverRequest);

        RouterFunction<ServerResponse> router = routerRest.routerFunction(handler);

        webTestClient.post()
                .uri("/api/v1/login")
                .bodyValue(handler)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

     */

}