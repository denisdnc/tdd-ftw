package com.tdd.user.gateways.database;

import com.tdd.user.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
