package com.openclassrooms.payMyBuddy.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

//TODO javadoc
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public User getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	public User getUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElse(null);
	}

	public void addUser(RegistrationForm registrationForm) throws Exception {
		if (userExistsByEmail(registrationForm.getEmail()) || userExistsByUsername(registrationForm.getUsername())) {
			log.warn("User already exists with email or username: {}", registrationForm.getEmail());
			throw new IllegalArgumentException("Nom d'utilisateur ou mail déjà utilisé.");
		}
		User user = new User(registrationForm);
		user.setPassword(registrationForm.getPassword());
		saveUser(user);
	}

	public boolean userExistsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public boolean userExistsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public User getUserByEmailOrUsername(String email, String username) {
		return userRepository.findByEmailOrUsername(email, username);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public boolean identifierIsValide(String email, String password) throws Exception {
		TrippleDes td = new TrippleDes();
		return userRepository.existsByEmailAndPasswordOrUsernameAndPassword(email, td.encrypt(password), email,
				td.encrypt(password));
	}

	public Map<String, String> validateAndUpdatePassword(String email, PasswordForm passwordForm) throws Exception {
		Map<String, String> response = new HashMap<>();
		TrippleDes td = new TrippleDes();
		User user = getUserByEmail(email);
		if (Objects.equals(td.encrypt(passwordForm.getOldPassword()), user.getPassword())) {
			if (Objects.equals(passwordForm.getPasswordConfirmation(), passwordForm.getPassword())) {
				user.setPassword(passwordForm.getPassword());
				saveUser(user);
				log.info("Password updated");
				response.put("status", "succsess");
				response.put("message", "Mot de passe mise à jour avec succès.");
			} else {
				log.warn("New password and password comfirmation not match");
				response.put("status", "error");
				response.put("message",
						"Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.");
			}
		} else {
			log.warn("Password not update ERROR old password fase");
			response.put("status", "error");
			response.put("message", "L'ancien mot de passe est incorrect.");
		}

		return response;
	}

	public Map<String, String> validateAndUpdateConnexion(String email, ConnexionForm connexionForm) {
		Map<String, String> response = new HashMap<>();

		User user = getUserByEmail(email);
		User connexion = getUserByEmail(connexionForm.getEmail());
		if (connexion == null) {
			log.warn("The user does not exist");
			response.put("message", "Utilisateur inconnu.");
			response.put("status", "error");
		} else if (Objects.equals(user.getEmail(), connexion.getEmail())) {
			log.warn("The user's email must be different from that of your");
			response.put("message", "L'utilisateur ne peut pas établir une connexion avec lui-même.");
			response.put("status", "error");
		} else if (user.getConnections().stream().anyMatch(c -> c.getEmail().equals(connexion.getEmail()))) {
			log.warn("The user is already connected to this user");
			response.put("message", "Utilisateur déjà ajouté.");
			response.put("status", "error");
		} else {
			response.put("message", "La relation avec " + connexion.getUsername() + " a été ajoutée avec succès.");
			response.put("status", "success");
			user.getConnections().add(connexion);
			saveUser(user);
		}
		return response;

	}

}
