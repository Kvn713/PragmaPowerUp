package co.com.pragma.crediya.usecase.login;

import co.com.pragma.crediya.model.login.Login;

public record LoginResponse(
        String accessToken,
        Long expire,
        Login login
) {
}
