package com.openclassrooms.payMyBuddy.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.payMyBuddy.constants.SessionConstants;
import com.openclassrooms.payMyBuddy.constants.UrlConstants;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.FlashAttribute;
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
	private final FlashAttribute flashAttribute;

	@GetMapping(UrlConstants.LOGIN)
	public String login(Model model) {

		log.info("Loading the login page");
		model.addAttribute("loginForm", new LoginForm());
		return UrlConstants.USER_LOGIN;
	}

	@PostMapping(UrlConstants.LOGIN)
	public String authentication(@Valid @ModelAttribute LoginForm loginForm, RedirectAttributes redirAttrs,
			HttpSession session) throws Exception {

		if (!userService.identifierAndPasswordIsValide(loginForm.getIdentifier(), loginForm.getPassword())) {
			log.info("Login error");
			flashAttribute.errorMessage(redirAttrs, "Identifiant ou mot de passe incorrecte.");
			return UrlConstants.REDIR_LOGIN;
		}
		User user = userService.getUserByEmailOrUsername(loginForm.getIdentifier(), loginForm.getIdentifier());
		session.setAttribute(SessionConstants.SESSION_ATTRIBUTE, user.getEmail());
		log.info("User authenticated successfully: {}", loginForm.getIdentifier());
		return UrlConstants.REDIR_TRANSACTION;
	}

	@GetMapping(UrlConstants.LOGOUT)
	public String logout(HttpServletRequest request) {

		log.info("Attempt to logout user ");
		request.getSession().removeAttribute(SessionConstants.SESSION_ATTRIBUTE);
		log.info("Username removed from the session. Do logout");
		return UrlConstants.REDIR_LOGIN;
	}

	@GetMapping(UrlConstants.REGISTRATION)
	public String createUser(Model model) {

		log.info("Loading the registration page");
		model.addAttribute("registrationForm", new RegistrationForm());
		return UrlConstants.USER_REGISTRATION;
	}

	@PostMapping(UrlConstants.REGISTRATION)
	public String createUser(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult result,
			RedirectAttributes redirAttrs) throws Exception {

		if (result.hasErrors()) {
			log.info("Invalid form");
			return UrlConstants.USER_REGISTRATION;
		}
		userService.addUser(registrationForm);
		log.debug("User successfully added: Username = {}, Email = {}", registrationForm.getUsername(),
				registrationForm.getEmail());
		flashAttribute.successMessage(redirAttrs, "Votre compte utilisateur a été créé avec succès.");
		return UrlConstants.REDIR_LOGIN;
	}
}
