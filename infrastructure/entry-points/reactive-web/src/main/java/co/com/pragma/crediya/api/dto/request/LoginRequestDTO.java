package co.com.pragma.crediya.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record LoginRequestDTO(
        UUID id,

        @NotNull(message = "Bad Request - email is null")
        @NotEmpty(message = "Bad Request - email is empty")
        @Email(message = "Bad Request - email bad format",
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String email,

        @NotNull(message = "Bad Request - password is null")
        @NotEmpty(message = "Bad Request - password is empty")
        String password,

        String role,
        List<String> permissions
) {
}
