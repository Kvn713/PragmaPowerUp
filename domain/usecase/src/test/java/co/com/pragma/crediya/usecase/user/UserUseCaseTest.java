package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserUseCase.class, Validations.class})
public class UserUseCaseTest {

    @InjectMocks
    private UserUseCase userUseCase;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Validations validations;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserTest() {
        User user = mock(User.class);

        when(userRepository.save(user)).thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.newUser(user);

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveUserErrorDuplicate() {
        User user = mock(User.class);

        when(userRepository.save(user)).thenReturn(Mono.error(new Throwable("Duplicate entry")));

        Mono<User> result = userUseCase.newUser(user);

        StepVerifier.create(result)
                .verifyError();
    }

    @Test
    void saveUserError() {
        User user = mock(User.class);

        when(userRepository.save(user)).thenReturn(Mono.error(new Throwable("Unknow")));

        Mono<User> result = userUseCase.newUser(user);

        StepVerifier.create(result)
                .verifyError();
    }

    @Test
    void getUserTest() {
        User user = mock(User.class);

        when(userRepository.validateDocument("123456")).thenReturn(Mono.just(true));
        when(userRepository.findByDocument("123456")).thenReturn(Mono.just(user));

        Mono<User> result = userUseCase.getUser("123456");

        StepVerifier.create(result)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void validationsTest() {

        Mono<Boolean> input = Mono.just(true);

        StepVerifier.create(Validations.validate(input))
                .verifyComplete();
    }

    @Test
    void validationsTestError() {

        Mono<Boolean> input = Mono.just(false);

        StepVerifier.create(Validations.validate(input))
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalArgumentException &&
                                throwable.getMessage().equals("Not Found user in database"))
                .verify();
    }

}
