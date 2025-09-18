package co.com.pragma.crediya.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.math.BigInteger;

public record CreateUserDTO(

        BigInteger idUser,

        @NotNull(message = "Bad Request - name is null")
        @NotEmpty(message = "Bad Request - name is empty")
        String name,

        @NotNull(message = "Bad Request - lastName is null")
        @NotEmpty(message = "Bad Request - lastName is empty")
        String lastName,
        String birthday,
        String address,
        String phone,

        @NotNull(message = "Bad Request - email is null")
        @NotEmpty(message = "Bad Request - email is empty")
        @Email(message = "Bad Request - email bad format",
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String email,

        @NotNull(message = "Bad Request - salary is null")
        @Range(message = "Bad Request - salary is out of range 0 - 15000000",
                min = 0, max = 15000000)
        BigDecimal salary,

        @NotNull(message = "Bad Request - document is null")
        @NotEmpty(message = "Bad Request - document is empty")
        String document,

        Long roleId,

        @NotNull(message = "Bad Request - password is null")
        @NotEmpty(message = "Bad Request - password is empty")
        String password
) {
}
