package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
        System.out.println("Greetings from Spring Boot!");
		return "Greetings from Spring Boot!";
	}

}
