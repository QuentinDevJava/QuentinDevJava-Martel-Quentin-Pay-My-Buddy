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

import com.openclassrooms.payMyBuddy.constants.SessionConstants;
import com.openclassrooms.payMyBuddy.constants.UrlConstants;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.FlashAttribute;
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
@RequestMapping(UrlConstants.USER)
@Slf4j
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final FlashAttribute flashAttribute;
	private static final String MESSAGE = "message";

	@GetMapping(UrlConstants.PROFIL)
	public String profil(Model model, HttpServletRequest request) {
		log.info("Loading the profile page");

		HttpSession session = request.getSession();
		ProfilForm profilForm = new ProfilForm();
		User user = userService.getUserByEmail(session.getAttribute(SessionConstants.SESSION_ATTRIBUTE).toString());

		profilForm.setUsername(user.getUsername());
		profilForm.setEmail(user.getEmail());
		model.addAttribute("profilForm", profilForm);
		return UrlConstants.USER_PROFIL;
	}

	@GetMapping(UrlConstants.UPDATE_PASSWORD)
	public String updatePassword(Model model, HttpServletRequest request) {

		log.info("Loading the password page");
		model.addAttribute("passwordForm", new PasswordForm());
		return UrlConstants.USER_PASSWORD;
	}

	@PostMapping(UrlConstants.UPDATE_PASSWORD)
	public String updatePassword(HttpServletRequest request, @Valid @ModelAttribute PasswordForm passwordForm,
			BindingResult result, RedirectAttributes redirAttrs) throws Exception {
		HttpSession session = request.getSession();

		if (result.hasErrors()) {
			log.info("Formulaire de modification du mdp invalide");
			return UrlConstants.USER_PASSWORD;
		}
		Map<String, String> response = userService.validateAndUpdatePassword(
				session.getAttribute(SessionConstants.SESSION_ATTRIBUTE).toString(), passwordForm);
		if ("success".equals(response.get("status"))) {
			flashAttribute.successMessage(redirAttrs, response.get(MESSAGE));
			return UrlConstants.REDIR_USER_PROFIL;
		} else {
			flashAttribute.errorMessage(redirAttrs, response.get(MESSAGE));
			return UrlConstants.REDIR_USER_UPDATE_PASSWORD;
		}
	}

	@GetMapping(UrlConstants.CONNECTION)
	public String connexion(HttpServletRequest request, Model model) {

		log.info("Loading the connexion page");
		model.addAttribute("connexionForm", new ConnexionForm());
		return UrlConstants.CONNECTION_CONNECTION;
	}

	@PostMapping(UrlConstants.CONNECTION)
	public String addConnexion(HttpServletRequest request, @Valid @ModelAttribute ConnexionForm connexionForm,
			BindingResult result, RedirectAttributes redirAttrs) {

		if (result.hasErrors()) {
			log.info("Formulaire d'ajout de relation invalide");
			return UrlConstants.CONNECTION_CONNECTION;
		}
		HttpSession session = request.getSession();
		Map<String, String> response = userService.validateAndUpdateConnexion(
				session.getAttribute(SessionConstants.SESSION_ATTRIBUTE).toString(), connexionForm);
		if ("success".equals(response.get("status"))) {
			flashAttribute.successMessage(redirAttrs, response.get(MESSAGE));
			return UrlConstants.REDIR_TRANSACTION;
		} else {
			flashAttribute.errorMessage(redirAttrs, response.get(MESSAGE));
			return UrlConstants.REDIR_USER_CONNECTION;
		}
	}
}
