package com.service.authentication.controllers.authenticaded;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.authentication.ApplicationConstants;
import com.service.authentication.utils.CallService;

@RestController
@RequestMapping("/authenticaded/client")
public class ClientAuthenticadedController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private CallService callService;

	@GetMapping("/get-client")
	public String getClientById() {
		List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(ApplicationConstants.CLIENT_SERVICE);

		if (serviceInstances.isEmpty()) {
            throw new IllegalStateException("No instances found for service: " + ApplicationConstants.CLIENT_SERVICE);
        }

		ServiceInstance serviceInstance = serviceInstances.get(0);
		String serviceResponse = this.callService.getForObject(serviceInstance.getUri(), ApplicationConstants.CLIENT_SERVICE_GET_CLIENT, String.class);

		return serviceResponse;
	}
}
