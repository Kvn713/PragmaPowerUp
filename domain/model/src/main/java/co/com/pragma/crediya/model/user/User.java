package co.com.pragma.crediya.model.user;
import co.com.pragma.crediya.model.role.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private BigInteger idUser;
    private String name;
    private String lastName;
    private String birthday;
    private String address;
    private String phone;
    private String email;
    private BigDecimal salary;
    private String document;
    private Long roleId;
    private String password;
}
