package com.tdd.user.gateways.database;

import com.tdd.user.domains.User;

public interface UserGateway {
    User save(User user);
}
