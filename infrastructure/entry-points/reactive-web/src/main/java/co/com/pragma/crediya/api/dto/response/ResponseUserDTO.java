package co.com.pragma.crediya.api.dto.response;

import java.math.BigDecimal;
import java.math.BigInteger;

public record ResponseUserDTO(
        BigInteger idUser,
        String name,
        String lastName,
        String birthday,
        String address,
        String phone,
        String email,
        BigDecimal salary,
        String document,
        Long roleId
) {
}
