package co.com.pragma.crediya.r2dbc;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserReactiveRepositoryAdapter.class, RoleReactiveRepositoryAdapter.class})
@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {
    // TODO: change four you own tests

    @InjectMocks
    UserReactiveRepositoryAdapter userRepositoryAdapter;

    @InjectMocks
    RoleReactiveRepositoryAdapter roleRepositoryAdapter;

    @Mock
    UserReactiveRepository userRepository;

    @Mock
    RoleReactiveRepository roleRepository;

    @Mock
    ObjectMapper mapper;

/*
    @Test
    void mustFindValueById() {

        when(userRepository.findById("1")).thenReturn(Mono.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Mono<Object> result = userRepositoryAdapter.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindAllValues() {
        when(repository.findAll()).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findAll();

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }

    @Test
    void mustFindByExample() {
        when(repository.findAll(any(Example.class))).thenReturn(Flux.just("test"));
        when(mapper.map("test", Object.class)).thenReturn("test");

        Flux<Object> result = repositoryAdapter.findByExample("test");

        StepVerifier.create(result)
                .expectNextMatches(value -> value.equals("test"))
                .verifyComplete();
    }*/


    @Test
    void saveUserTest() {
        BigInteger userId = new BigInteger("1");
        User user = new User(new BigInteger("1"), "John", "Doe",
                "01-01-2001","Calle 123", "1234567890"
                ,"correo@email.com", new BigDecimal("12000"), "123456789", 1L);
        UserEntity userEntity = new UserEntity();
        userEntity.setIdUsuario(userId);
        userEntity.setNombres("John");

        when(mapper.map(user, UserEntity.class)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(Mono.just(userEntity));
        when(mapper.mapBuilder(eq(userEntity), eq(User.UserBuilder.class))).thenReturn(user.toBuilder());

        Mono<User> result = userRepositoryAdapter.save(user);

        StepVerifier.create(result)
                .expectNextMatches(savedUser ->
                        savedUser != null &&
                                savedUser.getIdUsuario().equals(userId) &&
                                savedUser.getNombres().equals("John"))
                .verifyComplete();

        verify(userRepository).save(userEntity);
        verify(mapper).map(user, UserEntity.class);
        verify(mapper).mapBuilder(userEntity, User.UserBuilder.class);

    }


}
