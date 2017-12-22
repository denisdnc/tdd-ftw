package com.tdd.user.gateways.database;

import com.tdd.user.domains.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGatewayImpl implements UserGateway {

    private UserRepository userClient;
    @Autowired
    public UserGatewayImpl(UserRepository userClient) {
        this.userClient = userClient;
    }

    @Override
    public User save(User user) {
        return userClient.save(user);
    }
}
