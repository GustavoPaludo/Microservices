package com.service.authentication.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.service.authentication.ApplicationConstants;

@Service
public class DiscoveryClientsServiceImpl implements DiscoveryClientsService {

	private final DiscoveryClient discoveryClient;

    @Autowired
    public DiscoveryClientsServiceImpl(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

	@Override
	public ServiceInstance getServiceInstanceByServiceName(String serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

		if (instances.isEmpty()) {
		    throw new IllegalStateException("No instances found for service: " + serviceName);
		}

		ServiceInstance serviceInstance = instances.get(0);

		return serviceInstance;
	}
}
