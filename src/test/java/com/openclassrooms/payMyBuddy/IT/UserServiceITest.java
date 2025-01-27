package com.openclassrooms.paymybuddy.IT;

import static com.openclassrooms.paymybuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.paymybuddy.constants.AppConstants.OLD_PASSWORD_FALSE;
import static com.openclassrooms.paymybuddy.constants.AppConstants.PASSWORD_NOT_MATCH;
import static com.openclassrooms.paymybuddy.constants.AppConstants.PASSWORD_SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.UNKNOW_USER;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USER_ALREADY_ADDED;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USER_CANNOT_CONNECT_TO_THEMSELF;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.web.form.ConnexionForm;
import com.openclassrooms.paymybuddy.web.form.PasswordForm;
import com.openclassrooms.paymybuddy.web.form.RegistrationForm;

@SpringBootTest
@Transactional
 class UserServiceITest {

	@Autowired
	private UserService userService;

	private RegistrationForm form = new RegistrationForm();
	private RegistrationForm form2 = new RegistrationForm();

	private PasswordForm passwordForm = new PasswordForm();

	@BeforeEach
	 void setup() {

		form.setUsername("Test");
		form.setEmail("Test@test.fr");
		form.setPassword("Test1!78");

		passwordForm.setOldPassword("Test1!78");
		passwordForm.setPassword("Test1!78@");
		passwordForm.setPasswordConfirmation("Test1!78@");

		form2.setUsername("Test2");
		form2.setEmail("Test2@test.fr");
		form2.setPassword("Test1!78@");
	}

	@Test
	 void userServiceTest() throws Exception {

		userService.addUser(form);

		User foundUser = userService.getUserByEmailOrUsername(form.getEmail());

		assertNotNull(foundUser);
		assertEquals(foundUser.getEmail(), userService.getUserById(foundUser.getId()).getEmail());
		assertEquals("Test", foundUser.getUsername());
		assertEquals("Test@test.fr", foundUser.getEmail());

		assertEquals(userService.getUserByEmailOrUsername(foundUser.getUsername()).getId(),
				userService.getUserByEmailOrUsername(foundUser.getEmail()).getId());

		assertTrue(userService.userExistsByEmailOrUsername(foundUser.getEmail(),foundUser.getUsername()));
		assertTrue(userService.isValidCredentials(foundUser.getEmail(), "Test1!78"));
		assertTrue(userService.isValidCredentials(foundUser.getUsername(), "Test1!78"));

		assertThrows(IllegalArgumentException.class, () -> {
			userService.addUser(form);
		});
	}

	@Test
	 void validateAndUpdatePasswordTest() throws Exception {

		Map<String, String> response = new HashMap<>();
		response.put(SUCCESS, PASSWORD_SUCCESS);

		userService.addUser(form);

		User foundUser = userService.getUserByEmailOrUsername(form.getEmail());

		assertEquals(response, userService.validateAndUpdatePassword(foundUser.getEmail(), passwordForm));

		passwordForm.setOldPassword("Test1!78@");
		passwordForm.setPassword("Test1!78");
		passwordForm.setPasswordConfirmation("Test1!78@");

		response.clear();

		response.put(ERROR, PASSWORD_NOT_MATCH);
		assertEquals(response, userService.validateAndUpdatePassword(foundUser.getEmail(), passwordForm));

		passwordForm.setOldPassword("erreur");
		passwordForm.setPassword("Test1!78");
		passwordForm.setPasswordConfirmation("Test1!78");

		response.clear();

		response.put(ERROR, OLD_PASSWORD_FALSE);
		assertEquals(response, userService.validateAndUpdatePassword(foundUser.getEmail(), passwordForm));
	}

	@Test
	@Transactional
	 void validateAndUpdateConnexionTest() throws Exception {

		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail("Test2@test.fr");

		Map<String, String> response = new HashMap<>();
		response.put(SUCCESS, "La relation avec " + form2.getUsername() + " a été ajoutée avec succès.");

		userService.addUser(form);
		userService.addUser(form2);

		assertEquals(response, userService.addConnection(form.getEmail(), connexionForm));

		response.clear();
		response.put(ERROR, USER_ALREADY_ADDED);

		assertEquals(response, userService.addConnection(form.getEmail(), connexionForm));

		response.clear();
		response.put(ERROR, UNKNOW_USER);

		connexionForm.setEmail("error@error.fr");

		assertEquals(response, userService.addConnection(form.getEmail(), connexionForm));

		response.clear();
		response.put(ERROR, USER_CANNOT_CONNECT_TO_THEMSELF);

		connexionForm.setEmail("Test@test.fr");
		assertEquals(response, userService.addConnection(form.getEmail(), connexionForm));

	}

}
