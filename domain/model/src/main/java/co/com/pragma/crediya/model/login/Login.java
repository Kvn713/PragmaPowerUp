package co.com.pragma.crediya.model.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Login {
    private UUID id;
    private String email;
    private String password;
    private String role;
    private List<String> permissions;
}
