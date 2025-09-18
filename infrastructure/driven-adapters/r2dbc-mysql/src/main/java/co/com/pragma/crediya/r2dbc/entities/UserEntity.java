package co.com.pragma.crediya.r2dbc.entities;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.math.BigInteger;

@Table("User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("id_user")
    private BigInteger idUser;
    private String name;
    private String lastName;
    private String birthday;
    private String address;
    private String phone;
    private String email;
    private BigDecimal salary;
    private String document;
    @Column("role_id")
    private Long roleId;
    private String password;

}
