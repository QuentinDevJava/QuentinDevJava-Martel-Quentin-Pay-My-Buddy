package com.openclassrooms.paymybuddy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the PayMyBuddy application.
 * 
 * This class initializes the Spring Boot application by running the
 * {@link SpringApplication}.
 */
@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

	@Value("${encryption.key}")
	private String encryptionKey;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("#### SECRET KEY " + encryptionKey);
	}
}
