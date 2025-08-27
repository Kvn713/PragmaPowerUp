package co.com.pragma.crediya.api.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public record ResponseUserDTO(
        BigInteger idUsuario,
        String nombres,
        String apellidos,
        String fechaNacimiento,
        String direccion,
        String telefono,
        String correoElectronico,
        BigDecimal salarioBase,
        String documentoIdentidad,
        Long rolId) {
}
