package com.openclassrooms.payMyBuddy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.LoginForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//TODO javadoc

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

	private final UserService userService;

	@GetMapping("/login")
	public String login(Model model) {
		log.info("Loading the login page");
		model.addAttribute("loginForm", new LoginForm());
		return "user/login";

	}

	@PostMapping("/login")
	public String authentication(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session)
			throws Exception {
		if (userService.identifierIsValide(loginForm.getEmail(), loginForm.getPassword())) {
			User user = userService.getUserByEmailOrUsername(loginForm.getEmail(), loginForm.getEmail());
			session.setAttribute("username", user.getEmail());
			log.info("User authenticated successfully: {}", loginForm.getEmail());
			return "redirect:/transaction";
		}
		log.warn("Failed authentication attempt for email or username: {}", loginForm.getEmail());
		result.rejectValue("email", "error.loginForm", "Les informations de connexion fournies sont incorrectes.");
		return "user/login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		log.info("Attempt to logout user ");
		if (request.getSession() == null || request.getSession().getAttribute("username") == null) {
			return "redirect:/login";
		}
		request.getSession().removeAttribute("username");
		log.info("Username removed from the session. Do logout");
		return "redirect:/login";
	}

	@GetMapping("/registration")
	public String createUser(Model model) {
		log.info("Loading the registration page");
		model.addAttribute("registrationForm", new RegistrationForm());
		return "user/registration";
	}

	@PostMapping("/registration")
	public String createUser(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult result)
			throws IllegalArgumentException {
		try {
			userService.addUser(registrationForm);
			log.debug("User successfully added: Username = {}, Email = {}", registrationForm.getUsername(),
					registrationForm.getEmail());
			return "redirect:/login";
		} catch (IllegalArgumentException ex) {
			result.rejectValue("email", "error.loginForm", ex.getMessage());
			log.debug("Error while adding user: {}", ex.getMessage());
			return "user/registration";
		}
	}
}
