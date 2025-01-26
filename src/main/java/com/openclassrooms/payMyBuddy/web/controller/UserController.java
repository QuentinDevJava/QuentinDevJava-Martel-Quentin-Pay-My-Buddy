package com.openclassrooms.payMyBuddy.web.controller;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.CONNECTION;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.CONNECTION_CONNECTION;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.PROFIL;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.REDIR_USER_CONNECTION;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.REDIR_USER_PROFIL;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.REDIR_USER_UPDATE_PASSWORD;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.TRANSACTION_PAGE;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.UPDATE_PASSWORD;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.USER;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.USER_PASSWORD;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.USER_PROFIL;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.payMyBuddy.constants.AppConstants;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.FlashMessageHandler;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.ProfilForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * Controller responsible for handling user-related actions such as viewing and updating profile,
 * updating password, and managing user connections.
 */
@Controller
@RequestMapping(USER)
@Slf4j
@RequiredArgsConstructor
public class UserController {

	/** The user service. */
	private final UserService userService;
	
	/** The flash attribute. */
	private final FlashMessageHandler flashAttribute;
    /**
     * Displays the profile page for the logged-in user.
     * 
     * @param model The model used to pass data to the view.
     * @param request The HTTP request containing session information.
     * @return The profile view page.
     */
	@GetMapping(PROFIL)
	public String profil(Model model, HttpServletRequest request) {
		log.info("Loading the profile page");

		HttpSession session = request.getSession();
		ProfilForm profilForm = new ProfilForm();
		User user = userService.getUserByEmailOrUsername(session.getAttribute(SESSION_ATTRIBUTE).toString());

		profilForm.setUsername(user.getUsername());
		profilForm.setEmail(user.getEmail());
		model.addAttribute("profilForm", profilForm);
		return USER_PROFIL;
	}

    /**
     * Displays the password update page.
     * 
     * @param model The model used to pass data to the view.
     * @param request The HTTP request containing session information.
     * @return The password update view page.
     */
	@GetMapping(UPDATE_PASSWORD)
	public String updatePasswordForm(Model model, HttpServletRequest request) {

		log.info("Loading the password page");
		model.addAttribute("passwordForm", new PasswordForm());
		return USER_PASSWORD;
	}


    /**
     * Handles the password update form submission. If the form is valid, it attempts to update the password.
     * Displays a success or error message accordingly.
     * 
     * @param request The HTTP request containing session information.
     * @param passwordForm The password form submitted by the user.
     * @param result The result of form validation.
     * @param redirAttrs Attributes used to pass messages between redirects.
     * @return A redirect to the profile page or password update page with appropriate messages.
     * @throws Exception If an error occurs while updating the password.
     */
	@PostMapping(UPDATE_PASSWORD)
	public String updatePassword(HttpServletRequest request, @Valid @ModelAttribute PasswordForm passwordForm,
			BindingResult result, RedirectAttributes redirAttrs) throws Exception {

		if (result.hasErrors()) {
			log.debug("Invalid password change form.");
			return USER_PASSWORD;
		}

		HttpSession session = request.getSession();
		Map<String, String> response = userService.validateAndUpdatePassword(
				session.getAttribute(SESSION_ATTRIBUTE).toString(), passwordForm);

		if (response.containsKey(SUCCESS)) {
			log.info("Update password success : {}", response.get(SUCCESS));
			flashAttribute.successMessage(redirAttrs, response.get(SUCCESS));
			return REDIR_USER_PROFIL;
		} else {
			log.debug("Update password error : {}", response.get(ERROR));
			flashAttribute.errorMessage(redirAttrs, response.get(ERROR));
			return REDIR_USER_UPDATE_PASSWORD;
		}
	}

	   /**
     * Displays the connection page where the user can add connections.
     * 
     * @param request The HTTP request containing session information.
     * @param model The model used to pass data to the view.
     * @return The connection page view.
     */
	@GetMapping(CONNECTION)
	public String connexionForm(HttpServletRequest request, Model model) {

		log.info("Loading the connexion page");
		model.addAttribute("connexionForm", new ConnexionForm());
		return CONNECTION_CONNECTION;
	}
    /**
     * Handles the connection form submission. If the form is valid, it attempts to add a connection between the user and another user.
     * Displays a success or error message accordingly.
     * 
     * @param request The HTTP request containing session information.
     * @param connexionForm The connection form submitted by the user.
     * @param result The result of form validation.
     * @param redirAttrs Attributes used to pass messages between redirects.
     * @return A redirect to the transaction page or the connection page with appropriate messages.
     */
	@PostMapping(CONNECTION)
	public String addConnexion(HttpServletRequest request, @Valid @ModelAttribute ConnexionForm connexionForm,
			BindingResult result, RedirectAttributes redirAttrs) {

		if (result.hasErrors()) {
			log.debug("Invalid add relation form.");
			return CONNECTION_CONNECTION;
		}
		HttpSession session = request.getSession();
		Map<String, String> response = userService
				.addConnection(session.getAttribute(AppConstants.SESSION_ATTRIBUTE).toString(), connexionForm);
		if (response.containsKey(SUCCESS)) {
			log.info("Connection success : {}", response.get(SUCCESS));
			flashAttribute.successMessage(redirAttrs, response.get(SUCCESS));
			return TRANSACTION_PAGE;
		} else {
			log.debug("Connection error : {}", response.get(ERROR));
			flashAttribute.errorMessage(redirAttrs, response.get(ERROR));
			return REDIR_USER_CONNECTION;
		}
	}
}
