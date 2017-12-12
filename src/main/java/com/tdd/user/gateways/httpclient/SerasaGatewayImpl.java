package com.tdd.user.gateways.httpclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SerasaGatewayImpl implements SerasaGateway {

    private SerasaClient serasaClient;

    @Autowired
    public SerasaGatewayImpl(SerasaClient serasaClient) {
        this.serasaClient = serasaClient;
    }

    @Override
    public SerasaIntegrationStatus getStatus(String document) {
        return serasaClient.getStatus(document);
    }
}
