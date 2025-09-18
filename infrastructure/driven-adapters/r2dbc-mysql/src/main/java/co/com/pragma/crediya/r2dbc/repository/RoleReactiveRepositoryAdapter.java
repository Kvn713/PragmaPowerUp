package co.com.pragma.crediya.r2dbc.repository;

import co.com.pragma.crediya.model.role.Role;
import co.com.pragma.crediya.model.role.gateways.RoleRepository;
import co.com.pragma.crediya.r2dbc.entities.RoleEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RoleReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Role,
        RoleEntity,
        Long,
        RoleReactiveRepository> implements RoleRepository {

    protected RoleReactiveRepositoryAdapter(RoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d-> mapper.mapBuilder(d, Role.RoleBuilder.class).build());
    }
}
