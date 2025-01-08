package com.openclassrooms.payMyBuddy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		User user = userService.getUserByEmailOrUsername(loginForm.getIdentifier(), loginForm.getIdentifier());
		if (user == null) {
			result.rejectValue("identifier", "error.loginForm", "Identifiant inconnu.");
			return "user/login";
		}
		if (!userService.identifierIsValide(loginForm.getIdentifier(), loginForm.getPassword())) {
			result.rejectValue("identifier", "error.loginForm", "Le mot de passe est incorrecte.");
			return "user/login";
		}
		session.setAttribute("username", user.getEmail());
		log.info("User authenticated successfully: {}", loginForm.getIdentifier());
		return "redirect:/transaction";
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
	public String createUser(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult result,
			RedirectAttributes redirAttrs) {
		try {
			if (result.hasErrors()) {
				return "user/registration";
			}
			userService.addUser(registrationForm);
			log.debug("User successfully added: Username = {}, Email = {}", registrationForm.getUsername(),
					registrationForm.getEmail());
			redirAttrs.addFlashAttribute("successMessage", "Votre compte utilisateur a été créé avec succès.");
			return "redirect:/login";
		} catch (Exception ex) {
			result.rejectValue("email", "error.loginForm", ex.getMessage());
			log.debug("Error while adding user: {}", ex.getMessage());
			return "user/registration";
		}
	}
}
