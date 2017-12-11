package com.tdd.user.gateways.database;

import com.tdd.user.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDatabaseGatewayImpl implements UserDatabaseGateway {

    private UserRepository repository;

    @Autowired
    public UserDatabaseGatewayImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }
}
