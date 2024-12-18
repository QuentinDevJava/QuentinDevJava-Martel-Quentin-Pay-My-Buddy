package com.openclassrooms.payMyBuddy.web.controller;

import java.util.Objects;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.LoginForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/profile")
	public String profil(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();

		LoginForm loginForm = new LoginForm();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());
		loginForm.setUsername(user.getUsername());
		loginForm.setEmail(user.getEmail());
		model.addAttribute("loginForm", loginForm);
		if (model.containsAttribute("message")) {
			model.addAttribute("successMessage", model.asMap().get("message"));
		}
		return "user/profile";
	}

	@GetMapping("/updatePassword")
	public String updatePassword(Model model, HttpServletRequest request) {
		model.addAttribute("loginForm", new LoginForm());
		return "user/password";
	}

	@PostMapping("/updatePassword")
	public String updatePassword(HttpServletRequest request, @Valid @ModelAttribute LoginForm loginForm,
			BindingResult result, RedirectAttributes redirAttrs) throws Exception {

		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());

		if (Objects.equals(loginForm.getOldPassword(), user.getPassword())) {
			if (Objects.equals(loginForm.getPasswordConfirmation(), loginForm.getPassword())) {

				user.setPassword(loginForm.getPassword());
				userService.saveUser(user);
				log.info("Password updated");
				redirAttrs.addFlashAttribute("message", "Mot de passe mise à jour avec succès.");
				return "redirect:/user/profile";
			} else {
				log.debug("New password and password comfirmation not match");
				result.rejectValue("password", "error.loginForm",
						"Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.");
				return "user/password";
			}
		} else {
			log.debug("Password not update ERROR old password fase");
			result.rejectValue("oldPassword", "error.loginForm", "L'ancien mot de passe est incorrect.");
			return "user/password";

		}

	}

	@GetMapping("/connexion")
	public String connexion(HttpServletRequest request, Model model) {

		LoginForm loginForm = new LoginForm();
		loginForm.setConnexion(new User());
		model.addAttribute("loginForm", loginForm);

		return "/connexion/connexion";
	}

	@PostMapping("/connexion")
	public String addConnexion(HttpServletRequest request, @Valid @ModelAttribute LoginForm loginForm,
			BindingResult result) {
		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());
		User connection = userService.getUserByEmail(loginForm.getConnexion().getEmail());
		if (connection != null) {
			if (Objects.equals(user.getEmail(), connection.getEmail())) {
				log.warn("The user's email must be different from that of your");
				result.rejectValue("connexion.email", "error.loginForm",
						"L'utilisateur ne peut pas établir une connexion avec lui-même.");
				return "/connexion/connexion";
			}
			user.getConnections().add(connection);
			userService.saveUser(user);
			return "redirect:/transaction";
		} else {
			log.warn("The user does not exist");
			result.rejectValue("connexion.email", "error.loginForm", "Utilisateur inconnu.");
			return "/connexion/connexion";
		}
	}
}
