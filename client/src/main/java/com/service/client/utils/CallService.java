package com.service.client.utils;

import java.net.URI;

import org.springframework.core.ParameterizedTypeReference;

public interface CallService {

	public <T> T getForObject(URI serviceUri, String endpoint, Class<T> responseType);

	public <T> T getForObject(URI serviceUri, String endpoint, ParameterizedTypeReference<T> responseType);
}
