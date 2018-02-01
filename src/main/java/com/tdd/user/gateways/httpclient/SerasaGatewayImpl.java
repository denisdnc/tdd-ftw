package com.tdd.user.gateways.httpclient;

import com.tdd.user.domains.SerasaResponse;
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
    public SerasaResponse find(String document) {
        return serasahttpclient.getStatus(document);
    }
}
