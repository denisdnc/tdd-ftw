package com.tdd.user.gateways.httpclient;

import com.tdd.user.domains.SerasaWrapper;

public interface SerasaGateway {

    SerasaWrapper find(String document);
}
