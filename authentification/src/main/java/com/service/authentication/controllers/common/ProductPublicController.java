package com.service.authentication.controllers.common;

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
@RequestMapping("/public/product")
public class ProductPublicController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private CallService callService;

	@GetMapping("/get-product")
	public String getProductById() {
		List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(ApplicationConstants.PRODUCT_SERVICE);

		if (serviceInstances.isEmpty()) {
            throw new IllegalStateException("No instances found for service: " + ApplicationConstants.PRODUCT_SERVICE);
        }

		ServiceInstance serviceInstance = serviceInstances.get(0);
		String serviceResponse = this.callService.getForObject(serviceInstance.getUri(), ApplicationConstants.PRODUCT_SERVICE_GET_PRODUCT, String.class);

		return serviceResponse;
	}
}
