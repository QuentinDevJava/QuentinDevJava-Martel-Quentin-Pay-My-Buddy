package com.openclassrooms.payMyBuddy.web.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.LoginForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/profile")
	public String profil(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("username") == null) {
			log.info("Session is null");
			return "redirect:/login";
		}

		LoginForm loginForm = new LoginForm();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());
		loginForm.setUsername(user.getUsername());
		loginForm.setEmail(user.getEmail());
		model.addAttribute("loginForm", loginForm);

		return "user/profile";
	}

	@GetMapping("/updatePassword")
	public String updatePassword(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("username") == null) {
			log.info("SESSION is null");
			return "redirect:/login";
		}
		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);
		return "user/password";
	}

	@PostMapping("/updatePassword")
	public String updatePassword(HttpServletRequest request, @Valid @ModelAttribute LoginForm loginForm,
			BindingResult result) throws Exception {

		if (result.hasErrors()) {
			return "redirect:/user/profile";
		}

		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());

		if (Objects.equals(loginForm.getOldPassword(), user.getPassword())) {
			if (Objects.equals(loginForm.getPasswordConfirmation(), loginForm.getPassword())) {

				user.setPassword(loginForm.getPassword());
				userService.saveUser(user);
				log.info("Password updated");
			} else {
				log.info("New password and  password comfirmation not match");
			}
		} else {
			log.info("Password not update ERROR old password not match");
		}

		return "redirect:/user/profile";

	}

	@GetMapping("/connexion")
	public String connexion(HttpServletRequest request, Model model) {

		HttpSession session = request.getSession();
		if (session == null || session.getAttribute("username") == null) {
			log.info("Session is null");
			return "redirect:/login";
		}

		LoginForm loginForm = new LoginForm();
		model.addAttribute("loginForm", loginForm);

		return "/connexion/connexion";
	}

	@PostMapping("/connexion")
	public String addConnexion(HttpServletRequest request, @Valid @ModelAttribute LoginForm loginForm,
			BindingResult result) {

		if (result.hasErrors()) {
			return "redirect:/user/connexion";
		}

		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());

		User connection = userService.getUserByEmail(loginForm.getConnexion().getEmail());

		if (connection != null) {
			// Si l'utilisateur existe déjà
			if (Objects.equals(user.getEmail(), connection.getEmail())) {
				log.warn("The user's email must be different from that of your");
				result.rejectValue("connexion.email", "error.userDTO", "The user cannot connect to themselves.");
				return "redirect:/user/connexion";
			}
			user.getConnections().add(connection);
			userService.saveUser(user);
			return "redirect:/transaction";
		} else {
			// Si l'utilisateur n'existe pas dans la base de données
			log.warn("The user does not exist");
			result.rejectValue("connexion.email", "error.userDTO", "The user does not exist.");
			return "redirect:/user/connexion";
		}
	}
}
