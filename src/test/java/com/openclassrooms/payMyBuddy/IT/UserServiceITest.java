package com.openclassrooms.payMyBuddy.IT;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.OLD_PASSWORD_FALSE;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.PASSWORD_NOT_MATCH;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.PASSWORD_SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.UNKNOW_USER;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USER_ALREADY_ADDED;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.USER_CANNOT_CONNECT_TO_THEMSELF;
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

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

@SpringBootTest
 class UserServiceITest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

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

		User foundUser = userService.getUserByEmail(form.getEmail());

		assertNotNull(foundUser);
		assertEquals(foundUser.getEmail(), userService.getUserById(foundUser.getId()).getEmail());
		assertEquals("Test", foundUser.getUsername());
		assertEquals("Test@test.fr", foundUser.getEmail());
		assertEquals(userService.getUserByEmailOrUsername(null, foundUser.getUsername()).getId(),
				userService.getUserByEmailOrUsername(foundUser.getEmail(), null).getId());

		assertTrue(userService.userExistsByEmail(foundUser.getEmail()));
		assertTrue(userService.userExistsByUsername(foundUser.getUsername()));
		assertTrue(userService.identifierAndPasswordIsValide(foundUser.getEmail(), "Test1!78"));
		assertTrue(userService.identifierAndPasswordIsValide(foundUser.getUsername(), "Test1!78"));

		assertThrows(IllegalArgumentException.class, () -> {
			userService.addUser(form);
		});
		userRepository.delete(foundUser);
	}

	@Test
	 void validateAndUpdatePasswordTest() throws Exception {

		Map<String, String> response = new HashMap<>();
		response.put(SUCCESS, PASSWORD_SUCCESS);

		userService.addUser(form);

		User foundUser = userService.getUserByEmail(form.getEmail());

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
		userRepository.delete(foundUser);
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

		User foundUser = userService.getUserByEmail(form.getEmail());
		User foundUser2 = userService.getUserByEmail(form2.getEmail());
		userRepository.delete(foundUser);
		userRepository.delete(foundUser2);

	}

}
