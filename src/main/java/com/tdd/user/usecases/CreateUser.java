package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.SerasaWrapper;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserGateway;
import com.tdd.user.gateways.httpclient.SerasaGateway;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateUser {

    private UserGateway userGateway;
    private SerasaGateway serasaGateway;

    @Autowired
    public CreateUser(UserGateway userGateway, SerasaGateway serasaGateway) {

        this.userGateway = userGateway;
        this.serasaGateway = serasaGateway;
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
            SerasaWrapper serasaWrapper = serasaGateway.find(user.getDocument());
            user.setStatus(serasaWrapper.getStatus());
            userGateway.save(user);
        }

        return user;
    }
}
