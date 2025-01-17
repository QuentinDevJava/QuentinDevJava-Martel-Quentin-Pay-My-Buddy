package com.openclassrooms.payMyBuddy.UT;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.OLD_PASSWORD_FALSE;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.PASSWORD_NOT_MATCH;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.PASSWORD_SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.UNKNOW_USER;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USER_ALREADY_ADDED;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USER_CANNOT_CONNECT_TO_THEMSELF;
import static org.junit.Assert.assertTrue;
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

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

@SpringBootTest
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
		userService.identifierAndPasswordIsValide("test@test.fr", "Test1!78");
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

		assertEquals(OLD_PASSWORD_FALSE, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));
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

		assertEquals(PASSWORD_NOT_MATCH, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));
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

		assertEquals(PASSWORD_SUCCESS, response.get(SUCCESS));
		assertTrue(response.containsKey(SUCCESS));
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

		Map<String, String> response = userService.addConnection(email1, connexionForm);

		assertEquals("La relation avec " + mockUser2.getUsername() + " a été ajoutée avec succès.",
				response.get(SUCCESS));
		assertTrue(response.containsKey(SUCCESS));

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

		Map<String, String> response = userService.addConnection(email, connexionForm);

		assertEquals(USER_CANNOT_CONNECT_TO_THEMSELF, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));

	}

	@Test
	public void testValidateAndUpdateConnexionWhenUserNotExists() {

		String email = "Test@test.fr";
		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		Map<String, String> response = userService.addConnection(email, connexionForm);

		assertEquals(UNKNOW_USER, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));
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

		Map<String, String> response = userService.addConnection(user1.getEmail(), connexionForm);

		assertEquals(USER_ALREADY_ADDED, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));
	}
}
