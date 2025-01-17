package com.openclassrooms.payMyBuddy.IT;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.payMyBuddy.constants.AppConstants.SUCCESS;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

@SpringBootTest
@ActiveProfiles("local")
public class UserServiceITest {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;

	@Test
	public void userServiceTest() throws Exception {
		RegistrationForm form = new RegistrationForm();
		form.setUsername("Test");
		form.setEmail("Test@test.fr");
		form.setPassword("Test1!78");

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
	public void validateAndUpdatePasswordTest() throws Exception {
		RegistrationForm form = new RegistrationForm();
		form.setUsername("Test");
		form.setEmail("Test@test.fr");
		form.setPassword("Test1!78");

		PasswordForm passwordForm = new PasswordForm();
		passwordForm.setOldPassword("Test1!78");
		passwordForm.setPassword("Test1!78@");
		passwordForm.setPasswordConfirmation("Test1!78@");

		Map<String, String> response = new HashMap<>();

		response.put(SUCCESS, "Mot de passe mise à jour avec succès.");

		userService.addUser(form);

		User foundUser = userService.getUserByEmail(form.getEmail());

		assertEquals(response, userService.validateAndUpdatePassword(foundUser.getEmail(), passwordForm));

		passwordForm.setOldPassword("Test1!78@");
		passwordForm.setPassword("Test1!78");
		passwordForm.setPasswordConfirmation("Test1!78@");

		response.clear();

		response.put(ERROR, "Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.");
		assertEquals(response, userService.validateAndUpdatePassword(foundUser.getEmail(), passwordForm));

		passwordForm.setOldPassword("erreur");
		passwordForm.setPassword("Test1!78");
		passwordForm.setPasswordConfirmation("Test1!78");

		response.clear();

		response.put(ERROR, "L'ancien mot de passe est incorrect.");
		assertEquals(response, userService.validateAndUpdatePassword(foundUser.getEmail(), passwordForm));

		userRepository.delete(foundUser);

	}

	@Test
	@Transactional
	public void validateAndUpdateConnexionTest() throws Exception {
		RegistrationForm form = new RegistrationForm();
		form.setUsername("Test");
		form.setEmail("Test@test.fr");
		form.setPassword("Test1!78");
		RegistrationForm form2 = new RegistrationForm();
		form2.setUsername("Test2");
		form2.setEmail("Test2@test.fr");
		form2.setPassword("Test1!78@");

		ConnexionForm connexionForm = new ConnexionForm();
		connexionForm.setEmail("Test2@test.fr");

		Map<String, String> response = new HashMap<>();
		response.put(SUCCESS, "La relation avec " + form2.getUsername() + " a été ajoutée avec succès.");

		userService.addUser(form);
		userService.addUser(form2);

		User foundUser = userService.getUserByEmail(form.getEmail());
		User foundUser2 = userService.getUserByEmail(form2.getEmail());

		assertEquals(response, userService.addConnection(foundUser.getEmail(), connexionForm));

		response.clear();
		response.put(ERROR, "Utilisateur déjà ajouté.");

		assertEquals(response, userService.addConnection(foundUser.getEmail(), connexionForm));

		response.clear();
		response.put(ERROR, "Utilisateur inconnu.");

		connexionForm.setEmail("error@error.fr");

		assertEquals(response, userService.addConnection(foundUser.getEmail(), connexionForm));

		response.clear();
		response.put(ERROR, "L'utilisateur ne peut pas établir une connexion avec lui-même.");

		connexionForm.setEmail("Test@test.fr");
		assertEquals(response, userService.addConnection(foundUser.getEmail(), connexionForm));

		userRepository.delete(foundUser);
		userRepository.delete(foundUser2);

	}

}
