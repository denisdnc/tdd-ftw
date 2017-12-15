package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.User;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class CreateUser {

    public User execute(User user) {
        List<Error> errors = new ArrayList<>();
        if (StringUtils.isBlank(user.getName())) {
            errors.add(new Error("name is mandatory"));
        }

        if (StringUtils.isBlank(user.getDocument())) {
            errors.add(new Error("document is mandatory"));
        }

        user.setErrors(errors);
        return user;
    }
}
