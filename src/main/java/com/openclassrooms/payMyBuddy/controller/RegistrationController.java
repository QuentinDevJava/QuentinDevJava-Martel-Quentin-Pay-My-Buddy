package com.openclassrooms.payMyBuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/registration")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {

	@GetMapping
	public String index() {
		return "user/registration";
	}

}
