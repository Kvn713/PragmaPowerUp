package co.com.pragma.crediya.usecase.login;

import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.login.gateways.EncodeCredentials;
import co.com.pragma.crediya.model.login.gateways.LoginRepository;
import co.com.pragma.crediya.model.login.gateways.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = LoginUseCase.class)
public class LoginUseCaseTest {

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Mock
    private LoginRepository loginRepository;

    @Mock
    private EncodeCredentials encodeCredentials;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login(){
        Login login = mock(Login.class);
        LoginResponse loginResp = new LoginResponse("token", 0L, login);

        when(loginRepository.findByEmail(login)).thenReturn(Mono.just(login));
        when(encodeCredentials.matches(any(), any())).thenReturn(Mono.just(true));
        when(tokenRepository.generateToken(any())).thenReturn(Mono.just("token"));
        when(tokenRepository.expirationTime()).thenReturn(Mono.just(0L));

        Mono<LoginResponse> loginResponse = loginUseCase.login(login);

        StepVerifier.create(loginResponse)
                .expectNext(loginResp)
                .verifyComplete();
    }

}
