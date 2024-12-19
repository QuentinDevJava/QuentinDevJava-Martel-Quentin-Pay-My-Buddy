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

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

	private final UserService userService;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginForm", new LoginForm());
		return "user/login";

	}

	@PostMapping("/login")
	public String authentication(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session)
			throws Exception {
		if (userService.identifierIsValide(loginForm.getEmail(), loginForm.getPassword())) {
			User user = userService.getUserByEmailOrUsername(loginForm.getEmail(), loginForm.getEmail());
			session.setAttribute("username", user.getEmail());
			log.info("User find in database");
			return "redirect:/transaction";
		}
		log.info("Error username or email unknown");
		result.rejectValue("email", "error.loginForm", "Account does not exist or incorrect credentials.");
		return "user/login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		log.info("Attempt to logout user ");
		if (request.getSession() == null || request.getSession().getAttribute("username") == null) {
			return "redirect:/login";
		}
		request.getSession().removeAttribute("username");
		log.info("username removed from the session. Do logout");
		return "redirect:/login";
	}

	@GetMapping("/registration")
	public String createUser(Model model) {
		model.addAttribute("registrationForm", new RegistrationForm());
		return "user/registration";
	}

	@PostMapping("/registration")
	public String createUser(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult result)
			throws Exception {

		if (userService.userExistsByEmail(registrationForm.getEmail())
				|| userService.userExistsByUsername(registrationForm.getUsername())) {
			result.rejectValue("email", "error.loginForm", "Nom d'utilisateur ou mail deja utilisé.");
			return "user/registration";
		} else {
			User user = new User();
			user.setUsername(registrationForm.getUsername());
			user.setEmail(registrationForm.getEmail());
			user.setPassword(registrationForm.getPassword());
			userService.saveUser(user);
			return "redirect:/login";
		}
	}
}
