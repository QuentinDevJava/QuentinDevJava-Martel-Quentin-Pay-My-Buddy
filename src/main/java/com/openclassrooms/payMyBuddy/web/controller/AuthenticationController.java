package com.openclassrooms.payMyBuddy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TrippleDes;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.LoginForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthenticationController {
	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String login(Model model) {
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		return "user/login";

	}

	@PostMapping("/login")
	public String authentication(@Valid @ModelAttribute LoginForm loginForm, BindingResult result, HttpSession session)
			throws Exception {
		TrippleDes td = new TrippleDes();

		// TODO connexion possible mail ou username
		if (userService.existsByEmailAndPassword(loginForm.getEmail(), td.encrypt(loginForm.getPassword()))) {
			session.setAttribute("username", loginForm.getEmail());
			log.info("user are in database");
			return "redirect:/transaction";
		} else {
			log.info("user are not in database");

			// result.rejectValue("email", "error.userDTO", "Account does not exist or
			// incorrect credentials.");
			return "user/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) {
		log.info("Attempt to logout user ");
		if (request.getSession() == null || request.getSession().getAttribute("username") == null) {
			return "redirect:/login";
		}

		request.getSession().removeAttribute("username");
		log.info("Username removed from the session. do logout");
		return "redirect:/login";
	}

	@GetMapping("/registration")
	public String createUser(Model model) {
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		return "user/registration";
	}

	@PostMapping("/registration")
	public String createUser(@Valid @ModelAttribute LoginForm loginForm, BindingResult result) throws Exception {

		if (userService.existsByEmail(loginForm.getEmail()) || userService.existsByUsername(loginForm.getUsername())) {
			return "user/registration";
		} else {

			User user = new User();
			user.setUsername(loginForm.getUsername());
			user.setEmail(loginForm.getEmail());
			user.setPassword(loginForm.getPassword());
			userService.saveUser(user);
			return "redirect:/login";
		}

	}

}
