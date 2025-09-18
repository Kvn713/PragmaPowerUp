package co.com.pragma.crediya.api;

import co.com.pragma.crediya.api.dto.request.CreateUserDTO;
import co.com.pragma.crediya.api.dto.response.ResponseUserDTO;
import co.com.pragma.crediya.api.mapper.UserDTOMapper;
import co.com.pragma.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DTOTest {

    private final UserDTOMapper userMapper = Mappers.getMapper(UserDTOMapper.class);

    @Test
    void createUserDTO() {
        CreateUserDTO dto = new CreateUserDTO(new BigInteger("1"), "John", "Doe",
                "01-01-2001", "Calle 123", "1234567890",
                "correo@email.com", new BigDecimal("12000"),
                "123456789", 1L, "password");

        User user = userMapper.toModel(dto);

        assertNotNull(user);
        assertEquals("John", user.getName());
        assertEquals("Doe", user.getLastName());
        assertEquals("correo@email.com", user.getEmail());
        assertEquals("123456789", user.getDocument());
    }


    @Test
    void responseUserDTO() {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("correo@email.com");

        ResponseUserDTO response = userMapper.toResponse(user);

        assertNotNull(response);
        assertEquals("John Doe", response.name());
        assertEquals("correo@email.com", response.email());
    }
}
