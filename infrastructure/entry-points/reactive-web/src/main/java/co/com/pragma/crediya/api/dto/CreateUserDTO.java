package co.com.pragma.crediya.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.math.BigInteger;

public record CreateUserDTO(
        BigInteger idUsuario,

        @NotNull(message = "Bad request - nombres is null")
        @NotEmpty(message = "Bad request - nombres is empty")
        String nombres,

        @NotNull(message = "Bad request - apellidos is null")
        @NotEmpty(message = "Bad request - apellidos is empty")
        String apellidos,
        String fechaNacimiento,
        String direccion,
        String telefono,

        @NotNull(message = "Bad request - correoElectronico is null")
        @NotEmpty(message = "Bad request - correoElectronico is empty")
        @Email(message = "Bad request - correoElectronico bad format",
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String correoElectronico,

        @NotNull(message = "Bad request - salarioBase is null")
        @Range(message = "Bad request - salarioBase is out of range 0 - 15000000",
                min = 0, max = 15000000)
        BigDecimal salarioBase,

        String documentoIdentidad,
        Long rolId) {
}
