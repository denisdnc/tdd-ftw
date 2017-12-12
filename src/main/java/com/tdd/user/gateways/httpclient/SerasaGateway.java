package com.tdd.user.gateways.httpclient;

public interface SerasaGateway {
    SerasaIntegrationStatus getStatus(String document);
}
