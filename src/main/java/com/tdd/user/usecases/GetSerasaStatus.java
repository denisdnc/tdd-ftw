package com.tdd.user.usecases;

import com.tdd.user.domains.Error;
import com.tdd.user.domains.SerasaStatus;
import com.tdd.user.domains.SerasaStatusWrapper;
import com.tdd.user.domains.User;
import com.tdd.user.gateways.database.UserDatabaseGateway;
import com.tdd.user.gateways.httpclient.SerasaGateway;
import com.tdd.user.gateways.httpclient.SerasaIntegrationStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GetSerasaStatus {

    private UserDatabaseGateway userDatabaseGateway;
    private SerasaGateway serasaGateway;

    @Autowired
    public GetSerasaStatus(UserDatabaseGateway userDatabaseGateway, SerasaGateway serasaGateway) {
        this.userDatabaseGateway = userDatabaseGateway;
        this.serasaGateway = serasaGateway;
    }

    public SerasaStatusWrapper execute(String document) {

        if (StringUtils.isBlank(document)) {
            return createError("document is mandatory", document);
        }

        User user = userDatabaseGateway.findByDocument(document);

        if (user == null) {
            return createError("document not found", document);
        }

        SerasaIntegrationStatus serasaIntegrationStatus = serasaGateway.getStatus(document);

        for (SerasaStatus status : SerasaStatus.values()) {
            if (status.getValue().equals(serasaIntegrationStatus.getCodigoDeStatus())) {
                return new SerasaStatusWrapper(document, status, null);
            }
        }

        return createError("serasa integration status invalid", document);
    }

    private SerasaStatusWrapper createError(String message, String document) {
        return new SerasaStatusWrapper(document, null, Arrays.asList(new Error(message)));
    }

}
