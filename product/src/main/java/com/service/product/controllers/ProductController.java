package com.service.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.ApplicationConstants;
import com.service.product.utils.CallService;

@RestController
public class ProductController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private CallService callService;

	@GetMapping("/get-product")
	public String getProduct() {
		ServiceInstance serviceInstance = this.discoveryClient.getInstances(ApplicationConstants.CLIENT_SERVICE).get(0);
		String serviceResponse = this.callService.getForObject(serviceInstance.getUri(), ApplicationConstants.CLIENT_SERVICE_GET_CLIENT, String.class);
		return serviceResponse;
	}
}
