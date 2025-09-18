package co.com.pragma.crediya.model.role.gateways;

import co.com.pragma.crediya.model.role.Role;
import reactor.core.publisher.Mono;

public interface RoleRepository {
    Mono<Role> findById(Long id);
}
