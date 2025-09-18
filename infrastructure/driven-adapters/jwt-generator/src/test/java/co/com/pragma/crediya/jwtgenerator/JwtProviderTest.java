package co.com.pragma.crediya.jwtgenerator;

import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.model.login.gateways.TokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = JwtProvider.class)
public class JwtProviderTest {

    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtProvider = new JwtProvider();

        // Inyectar valores manualmente porque @Value no funciona fuera del contexto Spring
        ReflectionTestUtils.setField(jwtProvider, "secret", "mySecretKeyMySecretKeyMySecretKey"); // Debe ser >= 256 bits
        ReflectionTestUtils.setField(jwtProvider, "expiration", 3600L);
        ReflectionTestUtils.setField(jwtProvider, "issuer", "CrediYa");
    }

    @Test
    void expirationTimeTest(){
        StepVerifier.create(jwtProvider.expirationTime())
                .expectNext(3600L)
                .verifyComplete();
    }

    @Test
    void generateTokenTest(){
        Login login = Login.builder()
                .email("test@email.com")
                .password("password123")
                .role("USER")
                .permissions(new ArrayList<>())
                .build();

        StepVerifier.create(jwtProvider.generateToken(login))
                .expectNextMatches(token -> token != null && !token.isEmpty())
                .verifyComplete();

    }
}
