package com.service.client.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientService {

	@GetMapping("/helloWorld")
	public String helloWorld() {
		return "Hello World from Client Service";
	}
}
