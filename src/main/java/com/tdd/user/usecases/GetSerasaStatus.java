package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.SerasaStatus;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserDatabaseGateway;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GetSerasaStatus {

    private UserDatabaseGateway userDatabaseGateway;

    @Autowired
    public GetSerasaStatus(UserDatabaseGateway userDatabaseGateway) {
        this.userDatabaseGateway = userDatabaseGateway;
    }

    public SerasaStatus execute(String document) {

        if (StringUtils.isBlank(document)) {
            return createError("document is mandatory", document);
        }

        User user = userDatabaseGateway.findByDocument(document);

        if (user == null) {
            return createError("document not found", document);
        }

        // TODO
        return null;
    }

    private SerasaStatus createError(String message, String document) {
        return new SerasaStatus(document, null, Arrays.asList(new Error(message)));
    }

}
