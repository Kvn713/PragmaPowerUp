package co.com.pragma.crediya.r2dbc.entities;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("Rol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RoleEntity {
    @Id
    @Column("unique_id")
    private Long uniqueId;
    private String nombre;
    private String descripcion;

}
