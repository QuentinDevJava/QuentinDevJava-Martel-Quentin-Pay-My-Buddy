package com.openclassrooms.payMyBuddy.IT;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.ConnexionForm;
import com.openclassrooms.payMyBuddy.web.form.PasswordForm;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Test
	public void testGetProfil() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "TestPassword1!";

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setUsername(username);
		mockUser.setPassword(password);

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.getUserByEmail(email)).thenReturn(mockUser);

		mockMvc.perform(get("/user/profile").session(mockSession)).andExpect(status().isOk()).andDo(print())
				.andExpect(view().name("user/profile"));
	}

	@Test
	public void testGetUpdatePassword() throws Exception {

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", "test@test.com");

		mockMvc.perform(get("/user/updatePassword").session(mockSession)).andExpect(status().isOk()).andDo(print())
				.andExpect(view().name("user/password"));
	}

	@Test
	public void testPostUpdatePassword() throws Exception {
		String email = "Test@test.fr";
		String password = "TestPassword1!";
		String newPassword = "TestPassword2!";

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "Mot de passe mise à jour avec succès.");

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.validateAndUpdatePassword(eq(email), any(PasswordForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", password)
				.param("password", newPassword).param("passwordConfirmation", newPassword)).andDo(print())
				.andExpect(status().isFound()).andExpect(view().name("redirect:/user/profile"));
	}

	@Test
	public void testPostUpdatePasswordError() throws Exception {
		String email = "Test@test.fr";
		String password = "TestPassword1!";
		String newPassword = "TestPassword2!";

		Map<String, String> response = new HashMap<>();
		response.put("status", "error");
		response.put("message", "Erreur.");

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.validateAndUpdatePassword(eq(email), any(PasswordForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", password)
				.param("password", newPassword).param("passwordConfirmation", newPassword)).andDo(print())
				.andExpect(status().isFound()).andExpect(view().name("redirect:/user/updatePassword"));
	}

	@Test
	public void testPostUpdatePasswordErrorNewPasswordFormat() throws Exception {
		String email = "Test@test.fr";
		String password = "TestPassword1!";
		String newPassword = "TestPasswordErrorFormat";

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", password)
				.param("password", newPassword).param("passwordConfirmation", newPassword)).andExpect(status().isOk())
				.andDo(print()).andExpect(view().name("user/password"));
	}

	@Test
	public void testGetConnexion() throws Exception {

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", "test@test.com");

		mockMvc.perform(get("/user/connexion").session(mockSession)).andExpect(status().isOk()).andDo(print())
				.andExpect(view().name("/connexion/connexion"));
	}

	@Test
	public void testPostConnexion() throws Exception {
		String email = "Test@test.fr";

		Map<String, String> response = new HashMap<>();
		response.put("status", "success");
		response.put("message", "La relation a été ajoutée avec succès.");

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.validateAndUpdateConnexion(eq(email), any(ConnexionForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", email)).andDo(print())
				.andExpect(status().isFound()).andExpect(view().name("redirect:/transaction"));
	}

	@Test
	public void testPostConnexionError() throws Exception {
		String email = "Test@test.fr";

		Map<String, String> response = new HashMap<>();
		response.put("status", "error");
		response.put("message", "Erreur.");

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.validateAndUpdateConnexion(eq(email), any(ConnexionForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", email)).andDo(print())
				.andExpect(status().isFound()).andExpect(view().name("redirect:/user/connexion"));
	}

	@Test
	public void testPostConnexionErrorFormat() throws Exception {
		String email = "Test@test.fr";

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", "errorFormatEmail")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name("/connexion/connexion"));
	}

}
