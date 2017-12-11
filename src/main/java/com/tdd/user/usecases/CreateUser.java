package com.tdd.user.usecases;

import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserDatabaseGateway;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateUser {

    private UserDatabaseGateway userDatabaseGateway;

    @Autowired
    public CreateUser(UserDatabaseGateway userDatabaseGateway) {
        this.userDatabaseGateway = userDatabaseGateway;
    }

    public User execute(User user) {
        List<String> errors = new ArrayList<>();
        if (user.getName() == null) {
            errors.add("name is mandatory");
        }

        if (user.getDocument() == null) {
            errors.add("document is mandatory");
        }

        if (CollectionUtils.isNotEmpty(errors)) {
            user.setErrors(errors);
            return user;
        }

        return userDatabaseGateway.create(user);
    }
}
