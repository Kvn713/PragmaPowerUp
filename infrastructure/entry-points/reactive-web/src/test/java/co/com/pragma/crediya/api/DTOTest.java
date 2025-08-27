package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.api.mapper.UserDTOMapper;
import co.com.pragma.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class DTOTest {

    private final UserDTOMapper userMapper = Mappers.getMapper(UserDTOMapper.class);

    @Test
    void createUserDTO() {
        CreateUserDTO dto = new CreateUserDTO(new BigInteger("1"), "John", "Doe",
                "01-01-2001","Calle 123", "1234567890"
        ,"correo@email.com", new BigDecimal("12000"),
                "123456789", 1L);

        User user = userMapper.toModel(dto);

        assertNotNull(user);
        assertEquals("John", user.getNombres());
        assertEquals("Doe", user.getApellidos());
        assertEquals("correo@email.com", user.getCorreoElectronico());
        assertEquals("123456789", user.getDocumentoIdentidad());
    }


    @Test
    void responseUserDTO() {
        User user = new User();
        user.setNombres("John Doe");
        user.setCorreoElectronico("correo@email.com");

        ResponseUserDTO response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals("John Doe", response.nombres());
        assertEquals("correo@email.com", response.correoElectronico());
    }
}
