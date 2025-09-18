package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.request.CreateUserDTO;
import co.com.pragma.crediya.api.utils.RequestValidator;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class UtilsTest {

    @Test
    void globalExceptionTest() {
        Validator validator = mock(Validator.class);
        RequestValidator globalException = new RequestValidator(validator);
        CreateUserDTO dto = new CreateUserDTO(new BigInteger("1"), "John", "Doe",
                "01-01-2001", "Calle 123", "1234567890",
                "correo@email.com", new BigDecimal("12000"),
                "123456789", 1L, "password");

        doAnswer(invocation -> null).when(validator).validate(any(), any());

        Mono<CreateUserDTO> result = globalException.exceptionHandler(dto);

        StepVerifier.create(result)
                .expectNext(dto)
                .verifyComplete();

        verify(validator).validate(eq(dto), any(Errors.class));
    }

}
