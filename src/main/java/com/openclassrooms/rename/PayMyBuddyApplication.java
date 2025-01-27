package com.openclassrooms.rename;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PayMyBuddyApplication {

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "local");
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}
}
