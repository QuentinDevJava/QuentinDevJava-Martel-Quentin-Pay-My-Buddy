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
import com.openclassrooms.payMyBuddy.service.TrippleDes;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.ProfilForm;

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

		ProfilForm profilForm = new ProfilForm();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());

		profilForm.setUsername(user.getUsername());
		profilForm.setEmail(user.getEmail());
		model.addAttribute("profilForm", profilForm);
		if (model.containsAttribute("message")) {
			model.addAttribute("successMessage", model.asMap().get("message"));
		}
		return "user/profile";
	}

	@GetMapping("/updatePassword")
	public String updatePassword(Model model, HttpServletRequest request) {
		model.addAttribute("passwordForm", new PasswordForm());
		return "user/password";
	}

	@PostMapping("/updatePassword")
	public String updatePassword(HttpServletRequest request, @Valid @ModelAttribute PasswordForm passwordForm,
			BindingResult result, RedirectAttributes redirAttrs) throws Exception {
		TrippleDes td = new TrippleDes();
		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		if (Objects.equals(td.encrypt(passwordForm.getOldPassword()), user.getPassword())) {
			if (Objects.equals(passwordForm.getPasswordConfirmation(), passwordForm.getPassword())) {
				user.setPassword(passwordForm.getPassword());
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
		model.addAttribute("connexionForm", new ConnexionForm());
		return "/connexion/connexion";
	}

	@PostMapping("/connexion")
	public String addConnexion(HttpServletRequest request, @Valid @ModelAttribute ConnexionForm connexionForm,
			BindingResult result) {
		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		User connection = userService.getUserByEmail(connexionForm.getEmail());
		if (connection != null) {
			if (Objects.equals(user.getEmail(), connection.getEmail())) {
				log.warn("The user's email must be different from that of your");
				result.rejectValue("connexion.email", "error.connexion",
						"L'utilisateur ne peut pas établir une connexion avec lui-même.");
				return "/connexion/connexion";
			}
			user.getConnections().add(connection);
			userService.saveUser(user);
			return "redirect:/transaction";
		} else {
			log.warn("The user does not exist");
			result.rejectValue("connexion.email", "error.connexion", "Utilisateur inconnu.");
			return "/connexion/connexion";
		}
	}
}
