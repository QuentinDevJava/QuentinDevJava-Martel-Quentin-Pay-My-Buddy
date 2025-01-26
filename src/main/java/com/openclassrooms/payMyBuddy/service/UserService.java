package com.openclassrooms.payMyBuddy.service;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.OLD_PASSWORD_FALSE;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.PASSWORD_NOT_MATCH;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.PASSWORD_SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.UNKNOW_USER;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USERNAME_OR_EMAIL_IS_USE;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USER_ALREADY_ADDED;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USER_CANNOT_CONNECT_TO_THEMSELF;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.LoginForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing users.
 * 
 * This service provides methods to retrieve a user by their ID or email, 
 * add a new user, validate and update the password, or establish connections between users.
 * 
 * <p><b>Methods:</b></p>
 * <ul>
 *   <li><b>{@link #getUserById} :</b> Retrieves a user by their ID.</li>
 *   <li><b>{@link #getUserByEmail} :</b> Retrieves a user by their email.</li>
 *   <li><b>{@link #addUser} :</b> Adds a new user.</li>
 *   <li><b>{@link #userExistsByEmailOrUsername} :</b> Checks if a user exists with the specified email or username.</li>
 *   <li><b>{@link #getUserByEmailOrUsername} :</b> Retrieves a user by their email or username.</li>
 *   <li><b>{@link #isValidCredentials} :</b> Checks if the email (or username) and password are valid.</li>
 *   <li><b>{@link #validateAndUpdatePassword} :</b> Validates and updates the user's password.</li>
 *   <li><b>{@link #addConnection} :</b> Validates and adds a connection with another user.</li>
 *   <li><b>{@link #isAuthenticated} :</b> Authenticates a user based on login credentials (email or username).</li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	/**
	 * Retrieves a user by their ID.
	 * 
	 * @param id The ID of the user.
	 * @return The user found or null if no user is found.
	 */
	public User getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	/**
	 * Retrieves a user by their email.
	 * 
	 * @param email The email of the user.
	 * @return The user found or null if no user is found.
	 */
	public User getUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElse(null);
	}

	/**
	* Adds a new user to the system.
	* 
	* @param registrationForm The registration details of the user.
	* @throws IllegalArgumentException If a user with the same email or username already exists.
     */
	public void addUser(RegistrationForm registrationForm) throws IllegalArgumentException {
		if (userExistsByEmailOrUsername(registrationForm.getEmail(),registrationForm.getUsername())) {
			log.warn("User already exists : Username = {}, Email = {}", registrationForm.getUsername(), registrationForm.getEmail());
			throw new IllegalArgumentException(USERNAME_OR_EMAIL_IS_USE);
		}
		User user = new User(registrationForm);
		user.setPassword(passwordEncoder.encrypt(registrationForm.getPassword()));
		saveUser(user);
	}

	/**
	 * Checks if a user exists with the given email or username.
	 * 
	 * @param email The email of the user.
	 * @param username The username of the user.
	 * @return true if the user exists, false otherwise.
	 */
	public boolean userExistsByEmailOrUsername(String email,String username) {
		return userRepository.existsByEmailOrUsername(email, username);
	}

	/**
	* Retrieves a user by their email or username.
	* 
	* @param identifier The email or username of the user.
	* @return The user found or null if no user is found.
	*/
	public User getUserByEmailOrUsername(String identifier) {
		return userRepository.byUsernameOrEmail(identifier).orElse(null);
	}

	/**
	 * Saves a user in the database.
	 * 
	 * @param user The user to be saved.
	 */
	private void saveUser(User user) {
		userRepository.save(user);

	}

	/**
	 * Checks if the email (or username) and password are valid.
	 * 
	 * @param identifier The email or username of the user.
	 * @param password The password of the user.
	 * @return true if the email (or username) and password are valid, false otherwise.
	 */
	public boolean isValidCredentials(String identifier, String password) {
		return userRepository.existsByEmailOrUsernameAndPassword(identifier,identifier, passwordEncoder.encrypt(password));
	}

	/**
	* Validates and updates the user's password.
	* 
	* @param email The email of the user whose password is to be updated.
	* @param passwordForm The details of the new password.
	* @return A Map object containing the status and a success or error message.
	* @throws Exception If an error occurs during the validation or update.
	*/
	public Map<String, String> validateAndUpdatePassword(String identifier, PasswordForm passwordForm) throws Exception {
		Map<String, String> response = new HashMap<>();
		User user = getUserByEmailOrUsername(identifier);
		if (Objects.equals(passwordEncoder.encrypt(passwordForm.getOldPassword()), user.getPassword())) {
			if (Objects.equals(passwordForm.getPasswordConfirmation(), passwordForm.getPassword())) {
				user.setPassword(passwordEncoder.encrypt(passwordForm.getPassword()));
				saveUser(user);
				log.info("Password updated");
				response.put(SUCCESS, PASSWORD_SUCCESS);
			} else {
				log.warn("New password and password confirmation not match");
				response.put(ERROR, PASSWORD_NOT_MATCH);
			}
		} else {
			log.warn("Password not update error old password false");
			response.put(ERROR, OLD_PASSWORD_FALSE);
		}
		return response;
	}

	/**
	 * Adds a connection with another user.
	 * 
	 * @param identifier The email or username of the user who wants to add a connection.
	 * @param connexionForm The details of the user to connect with.
	 * @return A Map object containing the status and a success or error message.
	 */
	public Map<String, String> addConnection(String identifier, ConnexionForm connexionForm) {
		Map<String, String> response = new HashMap<>();

		User user = getUserByEmailOrUsername(identifier);
		User connexion = getUserByEmail(connexionForm.getEmail());

		if (connexion == null) {
			log.warn("The user does not exist");
			response.put(ERROR, UNKNOW_USER);
			return response;
		}

		if (user.getEmail().equals(connexion.getEmail())) {
			log.warn("The user's email must be different from that of your email");
			response.put(ERROR, USER_CANNOT_CONNECT_TO_THEMSELF);
			return response;
		}

		if (isConnectionExists(user, connexion)) {
			log.warn("The user is already connected to this user");
			response.put(ERROR, USER_ALREADY_ADDED);
			return response;
		}

		response.put(SUCCESS, "La relation avec " + connexion.getUsername() + " a été ajoutée avec succès.");
		user.getConnections().add(connexion);
		saveUser(user);
		log.info("User has been successfully added.");
		return response;
	}

	/**
	 * Checks if a connection exists between the current user and another user.
	 *
	 * @param user the current user
	 * @param connexion the user to check
	 * @return true if the connection exists, otherwise false
	 */
	private boolean isConnectionExists(User user, User connexion) {
	    return user.getConnections().stream().anyMatch(c -> c.getEmail().equals(connexion.getEmail()));
	}

	/**
	 * Authenticates a user using the provided login form.
	 *
	 * @param loginForm the login information
	 * @return true if authenticated, otherwise false
	 */
	public boolean isAuthenticated(LoginForm loginForm) {
	    log.info("attempting to authenticate {}", loginForm.getIdentifier());
	    User user = getUserByEmailOrUsername(loginForm.getIdentifier());
	    
	    if (user==null || !passwordMatch(loginForm.getPassword(), user.getPassword())) {
	        log.info("invalid credentials");
	        return false;
	    }

	    log.info("User authenticated");
	    return true;
	}

	/**
	 * Compares the entered password with the user's stored password.
	 *
	 * @param loginPassword the entered password
	 * @param userPassword the stored password
	 * @return true if the passwords match, otherwise false
	 */
	private boolean passwordMatch(String loginPassword, String userPassword) {
	    return passwordEncoder.encrypt(loginPassword).equals(userPassword);
	}

}
