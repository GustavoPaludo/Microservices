package com.service.client.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

	@GetMapping("/get-client")
	public String getClient() {
		return "Client String";
	}
}
