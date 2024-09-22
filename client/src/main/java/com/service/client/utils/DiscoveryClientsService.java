package com.service.client.utils;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import com.googlecode.ehcache.annotations.Cacheable;
import com.service.client.ApplicationConstants;

@Service
public class DiscoveryClientsService {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Cacheable(cacheName = ApplicationConstants.CACHE_FIVE_MINUTES)
	public ServiceInstance getServiceInstanceByServiceName(String serviceName) {
		List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);

		if (instances.isEmpty()) {
		    throw new IllegalStateException("No instances found for service: " + serviceName);
		}

		ServiceInstance serviceInstance = instances.get(new Random().nextInt(instances.size()));

		return serviceInstance;
	}
}
