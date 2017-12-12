package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserDatabaseGateway;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
        List<Error> errors = new ArrayList<>();
        if (StringUtils.isBlank(user.getName())) {
            errors.add(new Error("name is mandatory"));
        }

        if (StringUtils.isBlank(user.getDocument())) {
            errors.add(new Error("document is mandatory"));
        }

        if (CollectionUtils.isNotEmpty(errors)) {
            user.setErrors(errors);
            return user;
        }

        return userDatabaseGateway.create(user);
    }
}
