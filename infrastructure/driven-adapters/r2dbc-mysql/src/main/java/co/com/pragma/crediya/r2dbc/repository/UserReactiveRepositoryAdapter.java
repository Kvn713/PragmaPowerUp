package co.com.pragma.crediya.r2dbc.repository;

import co.com.pragma.crediya.model.user.User;
import co.com.pragma.crediya.model.user.gateways.UserRepository;
import co.com.pragma.crediya.r2dbc.entities.UserEntity;
import co.com.pragma.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User/* change for domain model */,
        UserEntity/* change for adapter model */,
        String,
        UserReactiveRepository
        > implements UserRepository {

    private final UserReactiveRepository userReactiveRepository;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper,
                                         UserReactiveRepository userReactiveRepository) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.mapBuilder(d, User.UserBuilder.class).build());
        this.userReactiveRepository = userReactiveRepository;
    }

    @Transactional
    @Override
    public Mono<User> save(User user) {
        return super.save(user);
    }

    @Override
    public Mono<Boolean> validateDocument(String document) {
        return userReactiveRepository.document(document);
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return userReactiveRepository.getByDocument(document);
    }

}
