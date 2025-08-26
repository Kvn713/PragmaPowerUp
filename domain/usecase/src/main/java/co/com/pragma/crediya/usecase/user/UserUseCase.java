package co.com.pragma.crediya.usecase.user;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.temporal.ValueRange;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class UserUseCase {
    private final UserRepository userRepository;

    public Mono<User> newUser(User user) {
        if (user.getNombres().isEmpty() || user.getNombres() == null) {
            return Mono.error(new Error("Bad request - nombres is empty or null"));
        }
        if (user.getApellidos().isEmpty() || user.getApellidos() == null) {
            return Mono.error(new Throwable("Bad request - apellidos is empty or null"));
        }
        if (user.getCorreoElectronico().isEmpty() || user.getCorreoElectronico() == null) {
            return Mono.error(new Throwable("Bad request - correoElectronico is empty or null"));
        }
        if (user.getSalarioBase().toString().isEmpty() ||user.getSalarioBase() == null) {
            return Mono.error(new Throwable("Bad request - salarioBase is empty or null"));
        }
        if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", user.getCorreoElectronico())){
            return Mono.error(new Throwable("Bad request - correoElectronico bad format"));
        }
        if (!ValueRange.of(0, 15000000).isValidValue(user.getSalarioBase().intValue())){
            return Mono.error(new Throwable("Bad request - salarioBase is out of range 0 - 15000000"));
        }
        return userRepository.save(user);
    }

}
