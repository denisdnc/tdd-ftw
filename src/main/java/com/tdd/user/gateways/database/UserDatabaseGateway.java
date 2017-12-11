package com.tdd.user.gateways.database;

import com.tdd.user.domains.User;

public interface UserDatabaseGateway {
    User create(User user);
    User findByDocument(String document);
}
