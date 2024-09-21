package com.service.product.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Service
public class CallServiceImpl implements CallService {

    private final RestClient restClient;

    @Autowired
    public CallServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public <T> T getForObject(URI serviceUri, String endpoint, Class<T> responseType) {
        return restClient.get().uri(serviceUri.toString() + endpoint).retrieve().body(responseType);
    }

    @Override
    public <T> T getForObject(URI serviceUri, String endpoint, ParameterizedTypeReference<T> responseType) {
        return restClient.get().uri(serviceUri.toString() + endpoint).retrieve().body(responseType);
    }
}
