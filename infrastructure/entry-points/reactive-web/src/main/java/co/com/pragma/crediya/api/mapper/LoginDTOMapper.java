package co.com.pragma.crediya.api.mapper;

import co.com.pragma.crediya.api.dto.request.LoginRequestDTO;
import co.com.pragma.crediya.api.dto.response.ResponseLoginDTO;
import co.com.pragma.crediya.api.dto.response.SignUpDTO;
import co.com.pragma.crediya.model.login.Login;
import co.com.pragma.crediya.usecase.login.LoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginDTOMapper {

    Login toModel(LoginRequestDTO loginRequestDTO);

    //@Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "tokenType",  constant = "Bearer")
    //@Mapping(target = "expire",  source = "expire")
    @Mapping(target = "user",        source = "login")
    ResponseLoginDTO toResponse(LoginResponse loginResponse);

    SignUpDTO toResponse(Login login);


}
