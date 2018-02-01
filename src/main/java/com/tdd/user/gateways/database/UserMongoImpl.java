package com.tdd.user.gateways.database;

import com.tdd.user.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMongoImpl implements UserGateway {
    private UserMongoRepository userMongoRepository;

    @Autowired
    public UserMongoImpl(UserMongoRepository userMongoRepository) {
        this.userMongoRepository = userMongoRepository;
    }


    @Override
    public User save(User user) {
        return userMongoRepository.save(user);
    }
}
