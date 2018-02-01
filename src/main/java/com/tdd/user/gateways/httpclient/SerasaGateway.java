package com.tdd.user.gateways.httpclient;

import com.tdd.user.domains.SerasaResponse;

public interface SerasaGateway {

    SerasaResponse find(String document);
}
