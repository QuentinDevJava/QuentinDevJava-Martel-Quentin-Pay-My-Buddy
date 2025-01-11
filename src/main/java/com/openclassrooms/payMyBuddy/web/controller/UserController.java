package com.openclassrooms.payMyBuddy.web.controller;

import java.util.Map;

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
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.ProfilForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO javadoc
@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/profile")
	public String profil(Model model, HttpServletRequest request) {
		log.info("Loading the profile page");

		HttpSession session = request.getSession();
		ProfilForm profilForm = new ProfilForm();
		User user = userService.getUserByEmail(session.getAttribute("username").toString());

		profilForm.setUsername(user.getUsername());
		profilForm.setEmail(user.getEmail());
		model.addAttribute("profilForm", profilForm);
		return "user/profile";
	}

	@GetMapping("/updatePassword")
	public String updatePassword(Model model, HttpServletRequest request) {
		log.info("Loading the password page");
		model.addAttribute("passwordForm", new PasswordForm());
		return "user/password";
	}

	@PostMapping("/updatePassword")
	public String updatePassword(HttpServletRequest request, @Valid @ModelAttribute PasswordForm passwordForm,
			BindingResult result, RedirectAttributes redirAttrs) throws Exception {
		HttpSession session = request.getSession();

		if (result.hasErrors()) {
			log.info("Formulaire modification mdp invalide");
			return "user/password";
		}

		// try {
		Map<String, String> response = userService
				.validateAndUpdatePassword(session.getAttribute("username").toString(), passwordForm);

		if ("succsess".equals(response.get("status"))) {
			redirAttrs.addFlashAttribute("successMessage", response.get("message"));
			return "redirect:/user/profile";
		} else {
			result.rejectValue("password", "error.passwordForm", response.get("message"));
			return "user/password";
		}
//		} catch (Exception e) {
//			result.rejectValue("password", "error.passwordForm", e.getMessage());
//			return "user/password";
//		}

	}

	@GetMapping("/connexion")
	public String connexion(HttpServletRequest request, Model model) {
		log.info("Loading the connexion page");
		model.addAttribute("connexionForm", new ConnexionForm());
		return "/connexion/connexion";
	}

	@PostMapping("/connexion")
	public String addConnexion(HttpServletRequest request, @Valid @ModelAttribute ConnexionForm connexionForm,
			BindingResult result, RedirectAttributes redirAttrs) {
		HttpSession session = request.getSession();

		Map<String, String> response = userService
				.validateAndUpdateConnexion(session.getAttribute("username").toString(), connexionForm);

		if ("success".equals(response.get("status"))) {
			redirAttrs.addFlashAttribute("successMessage", response.get("message"));
			return "redirect:/transaction";
		} else {
			result.rejectValue("email", "error.connexionForm", response.get("message"));
			return "/connexion/connexion";
		}
	}
}
