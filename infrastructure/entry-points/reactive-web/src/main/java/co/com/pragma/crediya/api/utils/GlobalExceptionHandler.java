package co.com.pragma.crediya.api.utils;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Validator validator;

    public <T> Mono<T> exceptionHandler(T dto) {
        return Mono.fromCallable(() -> {
            var err = new BeanPropertyBindingResult(dto, dto.getClass().getName());
            validator.validate(dto, err);
            if (err.hasErrors()) {
                throw new ValidationException(err.getAllErrors().getFirst().getDefaultMessage());
            }
            return dto;
        });
    }
}
