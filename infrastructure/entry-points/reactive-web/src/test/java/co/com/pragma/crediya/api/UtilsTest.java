package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.utils.GlobalExceptionHandler;
import co.com.pragma.crediya.model.user.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UtilsTest {

    @Test
    void globalExceptionTest(){
        Validator validator = mock(Validator.class);
        GlobalExceptionHandler globalException = new GlobalExceptionHandler(validator);
        CreateUserDTO dto = new CreateUserDTO(new BigInteger("1"), "John", "Doe",
                "01-01-2001","Calle 123", "1234567890"
                ,"correo@email.com", new BigDecimal("12000"),
                "123456789", 1L);

        doAnswer(invocation -> {
            Object obj = invocation.getArgument(0);
            Errors errors = invocation.getArgument(1);
            return null;
        }).when(validator).validate(any(), any());

        Mono<CreateUserDTO> result = globalException.exceptionHandler(dto);

        StepVerifier.create(result)
                .expectNext(dto)
                .verifyComplete();

        verify(validator).validate(eq(dto), any(Errors.class));
    }

}
