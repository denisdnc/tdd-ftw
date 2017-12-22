package com.tdd.user.gateways.httpclient;

import com.tdd.user.domains.SerasaWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SerasaGatewayImpl implements SerasaGateway {

    private SerasaHttpClient serasahttpclient;

    @Autowired
    public SerasaGatewayImpl(SerasaHttpClient serasahttpclient) {
        this.serasahttpclient = serasahttpclient;
    }

    @Override
    public SerasaWrapper find(String document) {
        return serasahttpclient.getStatus(document);
    }
}
