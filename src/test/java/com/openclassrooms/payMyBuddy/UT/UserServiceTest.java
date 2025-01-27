package com.openclassrooms.payMyBuddy.UT;

import static com.openclassrooms.paymybuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.paymybuddy.constants.AppConstants.OLD_PASSWORD_FALSE;
import static com.openclassrooms.paymybuddy.constants.AppConstants.PASSWORD_NOT_MATCH;
import static com.openclassrooms.paymybuddy.constants.AppConstants.PASSWORD_SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.UNKNOW_USER;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USER_ALREADY_ADDED;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USER_CANNOT_CONNECT_TO_THEMSELF;
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

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.PasswordEncoder;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.web.form.ConnexionForm;
import com.openclassrooms.paymybuddy.web.form.PasswordForm;
import com.openclassrooms.paymybuddy.web.form.RegistrationForm;

@SpringBootTest
 class UserServiceTest {

	@Mock
	UserRepository userRepository;
	
	@Mock
	PasswordEncoder passwordEncoder;

	@InjectMocks
	UserService userService;
	
	@Test
	 void testGetUserById() {
		userService.getUserById(1);
		verify(userRepository, times(1)).findById(1);
	}

	@Test
	 void testGetUserByEmailOrUsername() {
		
		userService.getUserByEmailOrUsername("test@test.fr");
		verify(userRepository, times(1)).byUsernameOrEmail(anyString());
	}

	@Test
	 void testidentifierIsValide() {
        when(passwordEncoder.encrypt("Test1!78")).thenReturn("encryptedPassword");
		userService.isValidCredentials("test@test.fr", "Test1!78");
		verify(userRepository, times(1)).existsByEmailOrUsernameAndPassword(anyString(), anyString(), anyString());
	}

	@Test
	 void testAddUser() throws Exception {
		// Given
		RegistrationForm form = new RegistrationForm();
		form.setEmail("Test@test.fr");
		form.setUsername("Test");
		form.setPassword("Test1!78");

		User mockUser = new User();
		mockUser.setEmail(form.getEmail());
		mockUser.setUsername(form.getUsername());

		// When
		when(userRepository.existsByEmailOrUsername(form.getEmail(),form.getUsername())).thenReturn(false);

		// Then
		userService.addUser(form);

		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	 void testAddUserWhenEmailOrUsernameAlreadyExists() {
		// Given
		RegistrationForm form = new RegistrationForm();
		form.setEmail("Test@test.fr");
		form.setUsername("Test");
		form.setPassword("Test1!78");

		User mockUser = new User();
		mockUser.setEmail(form.getEmail());
		mockUser.setUsername(form.getUsername());

		// When
		when(userRepository.existsByEmailOrUsername(form.getEmail(),form.getUsername())).thenReturn(true);

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			userService.addUser(form);
		});

		verify(userRepository, never()).save(any(User.class));
	}

	@Test
	 void testValidateAndUpdatePasswordWhenOldPasswordIsIncorrect() throws Exception {
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
	 void testValidateAndUpdatePasswordWhenNewPasswordIsNotEqualtoPasswordConfirmation() throws Exception {
		String email = "Test@test.fr";
		PasswordForm passwordForm = new PasswordForm();
		passwordForm.setOldPassword("wrongOldPassword!1");
		passwordForm.setPassword("NewPassword1!");
		passwordForm.setPasswordConfirmation("NewPassword1!Error");

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setPassword(passwordEncoder.encrypt("wrongOldPassword!1"));
		

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		Map<String, String> response = userService.validateAndUpdatePassword(email, passwordForm);

		assertEquals(PASSWORD_NOT_MATCH, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));
	}

	@Test
	 void testValidateAndUpdatePassword() throws Exception {
		String email = "Test@test.fr";
		PasswordForm passwordForm = new PasswordForm();
		passwordForm.setOldPassword("wrongOldPassword!1");
		passwordForm.setPassword("NewPassword1!");
		passwordForm.setPasswordConfirmation("NewPassword1!");

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setPassword(passwordEncoder.encrypt("wrongOldPassword!1"));

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

		Map<String, String> response = userService.validateAndUpdatePassword(email, passwordForm);

		assertEquals(PASSWORD_SUCCESS, response.get(SUCCESS));
		assertTrue(response.containsKey(SUCCESS));
	}

	@Test
	 void testValidateAndUpdateConnexion() {

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
	 void testValidateAndUpdateConnexionWhenUserIsTryingToConnectToHimself() {

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
	 void testValidateAndUpdateConnexionWhenUserNotExists() {

		String email = "Test@test.fr";
		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail(email);

		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		Map<String, String> response = userService.addConnection(email, connexionForm);

		assertEquals(UNKNOW_USER, response.get(ERROR));
		assertTrue(response.containsKey(ERROR));
	}

	@Test
	 void testValidateAndUpdateConnexionWhenUserIsAlreadyConnected() {
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
