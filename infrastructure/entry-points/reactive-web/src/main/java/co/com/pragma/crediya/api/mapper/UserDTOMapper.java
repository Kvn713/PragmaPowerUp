package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.CreateUserDTO;
import co.com.pragma.crediya.api.dto.ResponseUserDTO;
import co.com.pragma.crediya.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {

    User toModel(CreateUserDTO createUserDTO);
    ResponseUserDTO toResponse(User user);

}
