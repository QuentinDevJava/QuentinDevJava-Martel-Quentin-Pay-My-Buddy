package com.openclassrooms.paymybuddy.web.controller;
import static com.openclassrooms.paymybuddy.constants.AppConstants.LOGIN_ERROR;
import static com.openclassrooms.paymybuddy.constants.AppConstants.REGISTRATION_SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.LOGIN;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.LOGOUT;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_LOGIN;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_REGISTRATION;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REGISTRATION;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.TRANSACTION_PAGE;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.USER_LOGIN;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.USER_REGISTRATION;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.paymybuddy.service.FlashMessageHandler;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.web.form.LoginForm;
import com.openclassrooms.paymybuddy.web.form.RegistrationForm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller responsible for handling user authentication,
 * including login, registration, and logout functionalities.
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

	/** The user service. */
	private final UserService userService;
	
	/** The flash attribute. */
	private final FlashMessageHandler flashAttribute;

    /**
     * Displays the login page.
     * 
     * @param model The model used to pass data to the view.
     * @return The name of the login view.
     */
	@GetMapping(LOGIN)
	public String loginForm(Model model) {

		log.info("Loading the login page");
		model.addAttribute("loginForm", new LoginForm());
		return USER_LOGIN;
	}
	
    /**
     * Authenticates the user based on their login credentials.
     * If authentication fails, an error message is displayed.
     * 
     * @param loginForm The form containing the login credentials.
     * @param redirAttrs Attributes used to pass messages between redirects.
     * @param session The HTTP session of the user.
     * @return A redirect to the transaction page or back to the login page if an error occurs.
     */
	@PostMapping(LOGIN)
	public String authentication(@Valid @ModelAttribute LoginForm loginForm, RedirectAttributes redirAttrs, HttpSession session) {
		if (userService.isValidCredentials(loginForm.getIdentifier(),loginForm.getPassword())) {
			session.setAttribute(SESSION_ATTRIBUTE, loginForm.getIdentifier());
			log.info("User authenticated successfully: {}", loginForm.getIdentifier());
			return TRANSACTION_PAGE;
		}

		log.debug("Login error : {}", LOGIN_ERROR);
		flashAttribute.errorMessage(redirAttrs, LOGIN_ERROR);
		return REDIR_LOGIN;
	}
	
    /**
     * Logs out the user by removing their identifier from the session.
     * 
     * @param request The HTTP request.
     * @return A redirect to the login page after logging out.
     */
	@GetMapping(LOGOUT)
	public String logout(HttpServletRequest request) {

		log.info("Attempt to logout user ");
		request.getSession().removeAttribute(SESSION_ATTRIBUTE);
		log.info("Username removed from the session. Do logout");
		return REDIR_LOGIN;
	}


    /**
     * Displays the user registration page.
     * 
     * @param model The model used to pass data to the view.
     * @return The name of the registration view.
     */
	@GetMapping(REGISTRATION)
	public String registrationForm(Model model) {

		log.info("Loading the registration page");
		model.addAttribute("registrationForm", new RegistrationForm());
		return USER_REGISTRATION;
	}
	
    /**
     * Creates a new user after validating the registration form.
     * If there are errors in the form, the registration page is redisplayed.
     * 
     * @param registrationForm The form containing the registration details.
     * @param result The result of form validation.
     * @param redirAttrs Attributes used to pass messages between redirects.
     * @return A redirect to the login page after successful registration.
     */
	@PostMapping(REGISTRATION)
	public String createUser(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult result, RedirectAttributes redirAttrs) {

		if (result.hasErrors()) {
			log.debug("Invalid form");
			return USER_REGISTRATION;
		}
		try {
		userService.addUser(registrationForm);
		log.info("User successfully added: Username = {}, Email = {}", registrationForm.getUsername(),
				registrationForm.getEmail());
		flashAttribute.successMessage(redirAttrs, REGISTRATION_SUCCESS);
		return REDIR_LOGIN;

		}catch (Exception e) {
			log.debug("User already exists : Username = {}, Email = {}", registrationForm.getUsername(), registrationForm.getEmail());
			flashAttribute.errorMessage(redirAttrs, e.getMessage());
			return REDIR_REGISTRATION ;
		}
	}
}
