package com.openclassrooms.payMyBuddy.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;

	@PostMapping
	public ResponseEntity<String> createPerson(@Validated @RequestBody User user) throws URISyntaxException {

		log.info("POST request received for /user, adding user: {}", user);
		userService.addUser(user);
		log.info("user successfully created: {}", user);
		String str = "/user";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body("201");

	}
}
