package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.SerasaResponse;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserGateway;
import com.tdd.user.gateways.httpclient.SerasaGateway;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateUser {

    private UserGateway userRepository;
    private SerasaGateway serasaGateway;

    @Autowired
    public CreateUser(UserGateway userRepository, SerasaGateway serasaGateway) {
        this.userRepository = userRepository;
        this.serasaGateway = serasaGateway;
    }

    public User execute(User user){
        List<Error> errors = new ArrayList<>();

        if (StringUtils.isBlank(user.getDocument())) {
            errors.add(new Error("CPF é obrigatório"));
        }
        if (StringUtils.isBlank(user.getName())) {
            errors.add(new Error("Nome é obrigatório"));
        }
        if (CollectionUtils.isEmpty(errors)) {
            SerasaResponse serasaResponse = serasaGateway.find(user.getDocument());
            user.setStatus(serasaResponse.getStatus());
            userRepository.save(user);
        }
        user.setErrors(errors);

        return user;
    }

}
