package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserGateway;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class CreateUser {

    private UserGateway userGateway;

    @Autowired
    public CreateUser(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(User user) {
        List<Error> errors = new ArrayList<>();
        if (StringUtils.isBlank(user.getName())) {
            errors.add(new Error("name is mandatory"));
        }

        if (StringUtils.isBlank(user.getDocument())) {
            errors.add(new Error("document is mandatory"));
        }

        user.setErrors(errors);

        if (CollectionUtils.isEmpty(errors)) {
            userGateway.save(user);
        }

        return user;
    }
}
