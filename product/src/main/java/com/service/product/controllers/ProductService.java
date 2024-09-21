package com.service.product.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.product.utils.CallService;

@RestController
public class ProductService {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private CallService callService;

	@GetMapping("helloEureka")
	public String helloWorld() {
		ServiceInstance serviceInstance = this.discoveryClient.getInstances("client-service").get(0);
		String serviceResponse = this.callService.getForObject(serviceInstance.getUri(), "/helloWorld", String.class);
		return serviceResponse;
	}
}
