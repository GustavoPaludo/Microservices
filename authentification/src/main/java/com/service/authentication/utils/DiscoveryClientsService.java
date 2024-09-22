package com.service.authentication.utils;

import org.springframework.cloud.client.ServiceInstance;

public interface DiscoveryClientsService {

	public ServiceInstance getServiceInstanceByServiceName(String serviceName);
}
