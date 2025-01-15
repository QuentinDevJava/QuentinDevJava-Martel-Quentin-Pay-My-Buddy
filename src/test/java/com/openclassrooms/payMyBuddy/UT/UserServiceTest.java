package com.openclassrooms.payMyBuddy.UT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

@SpringBootTest
@ActiveProfiles("local")
public class UserServiceTest {

	@Mock
	UserRepository userRepository;

	@InjectMocks
	UserService userService;

	@Test
	public void testGetUserById() {
		userService.getUserById(1);
		verify(userRepository, times(1)).findById(1);
	}

	@Test
	public void testGetUserByEmailOrUsername() {
		userService.getUserByEmailOrUsername("test@test.fr", "Test");
		verify(userRepository, times(1)).findByEmailOrUsername(anyString(), anyString());
	}

	@Test
	public void testidentifierIsValide() throws Exception {
		userService.identifierIsValide("test@test.fr", "Test1!78");
		verify(userRepository, times(1)).existsByEmailAndPasswordOrUsernameAndPassword(anyString(), anyString(),
				anyString(), anyString());
	}

	@Test
	public void testAddUser() throws Exception {
		// Given
		RegistrationForm form = new RegistrationForm();
		form.setEmail("Test@test.fr");
		form.setUsername("Test");
		form.setPassword("Test1!78");

		User mockUser = new User();
		mockUser.setEmail(form.getEmail());
		mockUser.setUsername(form.getUsername());

		// When
		when(userRepository.existsByEmail(form.getEmail())).thenReturn(false);
		when(userRepository.existsByUsername(form.getUsername())).thenReturn(false);

		// Then
		userService.addUser(form);

		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void testAddUserWhenEmailAlreadyExists() {
		// Given
		RegistrationForm form = new RegistrationForm();
		form.setEmail("Test@test.fr");
		form.setUsername("Test");
		form.setPassword("Test1!78");

		User mockUser = new User();
		mockUser.setEmail(form.getEmail());
		mockUser.setUsername(form.getUsername());

		// When
		when(userRepository.existsByEmail(form.getEmail())).thenReturn(true);
		when(userRepository.existsByUsername(form.getUsername())).thenReturn(false);

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			userService.addUser(form);
		});

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	public void testAddUserWhenUsernameAlreadyExists() {
		// Given
		RegistrationForm form = new RegistrationForm();
		form.setEmail("Test@test.fr");
		form.setUsername("Test");
		form.setPassword("Test1!78");

		User mockUser = new User();
		mockUser.setEmail(form.getEmail());
		mockUser.setUsername(form.getUsername());

		// When
		when(userRepository.existsByEmail(form.getEmail())).thenReturn(false);
		when(userRepository.existsByUsername(form.getUsername())).thenReturn(true);

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			userService.addUser(form);
		});

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	public void testValidateAndUpdatePasswordWhenOldPasswordIsIncorrect() throws Exception {
		String email = "Test@test.fr";
		PasswordForm passwordForm = new PasswordForm();
		passwordForm.setOldPassword("wrongOldPassword!1");
		passwordForm.setPassword("NewPassword1!");
		passwordForm.setPasswordConfirmation("NewPassword1!");

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setPassword("EncryptedOldPassword!1");

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		Map<String, String> response = userService.validateAndUpdatePassword(email, passwordForm);

		assertEquals("L'ancien mot de passe est incorrect.", response.get("message"));
		assertEquals("error", response.get("status"));
	}

	@Test
	public void testValidateAndUpdatePasswordWhenNewPasswordIsNotEqualtoPasswordConfirmation() throws Exception {
		String email = "Test@test.fr";
		PasswordForm passwordForm = new PasswordForm();
		passwordForm.setOldPassword("wrongOldPassword!1");
		passwordForm.setPassword("NewPassword1!");
		passwordForm.setPasswordConfirmation("NewPassword1!Error");

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setPassword("wrongOldPassword!1");

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		Map<String, String> response = userService.validateAndUpdatePassword(email, passwordForm);

		assertEquals("Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.",
				response.get("message"));
		assertEquals("error", response.get("status"));
	}

	@Test
	public void testValidateAndUpdatePassword() throws Exception {
		String email = "Test@test.fr";
		PasswordForm passwordForm = new PasswordForm();
		passwordForm.setOldPassword("wrongOldPassword!1");
		passwordForm.setPassword("NewPassword1!");
		passwordForm.setPasswordConfirmation("NewPassword1!");

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setPassword("wrongOldPassword!1");

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		Map<String, String> response = userService.validateAndUpdatePassword(email, passwordForm);

		assertEquals("Mot de passe mise à jour avec succès.", response.get("message"));
		assertEquals("success", response.get("status"));
	}

	@Test
	public void testValidateAndUpdateConnexion() {

		String email1 = "Test@test.fr";
		String email2 = "Test1@test.fr";

		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail(email2);

		User mockUser1 = new User();
		mockUser1.setEmail(email1);
		mockUser1.setUsername("Test");

		User mockUser2 = new User();
		mockUser2.setEmail(email2);
		mockUser2.setUsername("Test");

		when(userRepository.findByEmail(email1)).thenReturn(Optional.of(mockUser1));
		when(userRepository.findByEmail(email2)).thenReturn(Optional.of(mockUser2));

		Map<String, String> response = userService.validateAndUpdateConnexion(email1, connexionForm);

		assertEquals("La relation avec " + mockUser2.getUsername() + " a été ajoutée avec succès.",
				response.get("message"));
		assertEquals("success", response.get("status"));

	}

	@Test
	public void testValidateAndUpdateConnexionWhenUserIsTryingToConnectToHimself() {

		String email = "Test@test.fr";
		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail(email);

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setUsername("Test");

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		Map<String, String> response = userService.validateAndUpdateConnexion(email, connexionForm);

		assertEquals("L'utilisateur ne peut pas établir une connexion avec lui-même.", response.get("message"));
		assertEquals("error", response.get("status"));

	}

	@Test
	public void testValidateAndUpdateConnexionWhenUserNotExists() {

		String email = "Test@test.fr";
		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		Map<String, String> response = userService.validateAndUpdateConnexion(email, connexionForm);

		assertEquals("Utilisateur inconnu.", response.get("message"));
		assertEquals("error", response.get("status"));
	}

	@Test
	public void testValidateAndUpdateConnexionWhenUserIsAlreadyConnected() {
		String email1 = "Test@test.fr";
		String email2 = "Test1@test.fr";
		String username1 = "Test1";
		String username2 = "Test2";

		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail(email2);

		User user1 = new User();
		user1.setEmail(email1);
		user1.setUsername(username1);

		User user2 = new User();
		user2.setEmail(email2);
		user2.setUsername(username2);

		Set<User> connexions = new HashSet<>();
		connexions.add(user2);

		user1.setConnections(connexions);

		when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
		when(userRepository.findByEmail(connexionForm.getEmail())).thenReturn(Optional.of(user2));

		Map<String, String> response = userService.validateAndUpdateConnexion(user1.getEmail(), connexionForm);

		assertEquals("Utilisateur déjà ajouté.", response.get("message"));
		assertEquals("error", response.get("status"));
	}
}
