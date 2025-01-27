package com.openclassrooms.paymybuddy.IT;

import static com.openclassrooms.paymybuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.paymybuddy.constants.AppConstants.OLD_PASSWORD_FALSE;
import static com.openclassrooms.paymybuddy.constants.AppConstants.PASSWORD_NOT_MATCH;
import static com.openclassrooms.paymybuddy.constants.AppConstants.PASSWORD_SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.UNKNOW_USER;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USER_ALREADY_ADDED;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USER_CANNOT_CONNECT_TO_THEMSELF;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.CONNECTION_CONNECTION;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_USER_CONNECTION;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_USER_PROFIL;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_USER_UPDATE_PASSWORD;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.TRANSACTION_PAGE;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.USER_PASSWORD;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.USER_PROFIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.web.form.ConnexionForm;
import com.openclassrooms.paymybuddy.web.form.PasswordForm;

@SpringBootTest
@AutoConfigureMockMvc
 class UserControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	private String email = "Test@test.fr";
	private String username = "Test";
	private String password = "TestPassword1!";
	private MockHttpSession mockSession = new MockHttpSession();

	@BeforeEach
	 void setup() {

		mockSession.setAttribute(SESSION_ATTRIBUTE, email);
	}

	@Test
	 void testGetProfil() throws Exception {

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setUsername(username);
		mockUser.setPassword(password);

		when(userService.getUserByEmail(email)).thenReturn(mockUser);

		mockMvc.perform(get("/user/profile").session(mockSession)).andExpect(status().isOk()).andDo(print())
				.andExpect(view().name(USER_PROFIL));
	}

	@Test
	 void testGetUpdatePassword() throws Exception {

		mockMvc.perform(get("/user/updatePassword").session(mockSession)).andExpect(status().isOk()).andDo(print())
				.andExpect(view().name(USER_PASSWORD));
	}

	@Test
	 void testPostUpdatePassword() throws Exception {

		String newPassword = "TestPassword2!";
		Map<String, String> response = new HashMap<>();
		response.put(SUCCESS, PASSWORD_SUCCESS);

		when(userService.validateAndUpdatePassword(eq(email), any(PasswordForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", password)
				.param("password", newPassword).param("passwordConfirmation", newPassword)).andDo(print())
				.andExpect(flash().attribute(SUCCESS, PASSWORD_SUCCESS)).andExpect(status().isFound())
				.andExpect(view().name(REDIR_USER_PROFIL));
	}

	@Test
	 void testPostUpdatePasswordErrorNotMatch() throws Exception {

		String newPassword1 = "TestPassword2!";
		String newPassword2 = "TestPassword3!";
		Map<String, String> response = new HashMap<>();
		response.put(ERROR, PASSWORD_NOT_MATCH);

		when(userService.validateAndUpdatePassword(eq(email), any(PasswordForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", password)
				.param("password", newPassword1).param("passwordConfirmation", newPassword2)).andDo(print())
				.andExpect(flash().attribute(ERROR, PASSWORD_NOT_MATCH)).andExpect(status().isFound())
				.andExpect(status().isFound()).andExpect(view().name(REDIR_USER_UPDATE_PASSWORD));
	}

	@Test
	 void testPostUpdatePasswordErrorOldPasswordFalse() throws Exception {

		String newPassword = "TestPassword2!";
		Map<String, String> response = new HashMap<>();
		response.put(ERROR, OLD_PASSWORD_FALSE);

		when(userService.validateAndUpdatePassword(eq(email), any(PasswordForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", newPassword)
				.param("password", newPassword).param("passwordConfirmation", newPassword)).andDo(print())
				.andExpect(flash().attribute(ERROR, OLD_PASSWORD_FALSE)).andExpect(status().isFound())
				.andExpect(status().isFound()).andExpect(view().name(REDIR_USER_UPDATE_PASSWORD));
	}

	@Test
	 void testPostUpdatePasswordErrorNewPasswordFormat() throws Exception {

		String newPassword = "TestPasswordErrorFormat";

		mockMvc.perform(post("/user/updatePassword").session(mockSession).param("oldPassword", password)
				.param("password", newPassword).param("passwordConfirmation", newPassword)).andExpect(status().isOk())
				.andDo(print()).andExpect(view().name(USER_PASSWORD));
	}

	@Test
	 void testGetConnexion() throws Exception {

		mockMvc.perform(get("/user/connexion").session(mockSession)).andExpect(status().isOk()).andDo(print())
				.andExpect(view().name(CONNECTION_CONNECTION));
	}

	@Test
	 void testPostConnexion() throws Exception {

		Map<String, String> response = new HashMap<>();
		response.put(SUCCESS, "La relation avec " + email + " a été ajoutée avec succès.");

		when(userService.addConnection(eq(email), any(ConnexionForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", email)).andDo(print())
				.andExpect(flash().attribute(SUCCESS, response.get(SUCCESS))).andExpect(status().isFound())
				.andExpect(view().name(TRANSACTION_PAGE));
	}

	@Test
	 void testPostConnexionError() throws Exception {

		Map<String, String> response = new HashMap<>();
		response.put(ERROR, USER_CANNOT_CONNECT_TO_THEMSELF);

		when(userService.addConnection(eq(email), any(ConnexionForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", email)).andDo(print())
				.andExpect(flash().attribute(ERROR, response.get(ERROR))).andExpect(status().isFound())
				.andExpect(status().isFound()).andExpect(view().name(REDIR_USER_CONNECTION));
	}

	@Test
	 void testPostConnexionErrorUnknowUser() throws Exception {

		Map<String, String> response = new HashMap<>();
		response.put(ERROR, UNKNOW_USER);

		when(userService.addConnection(eq(email), any(ConnexionForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", "unknow@test.com")).andDo(print())
				.andExpect(flash().attribute(ERROR, response.get(ERROR))).andExpect(status().isFound())
				.andExpect(status().isFound()).andExpect(view().name(REDIR_USER_CONNECTION));
	}

	@Test
	 void testPostConnexionErrorUserAlreadyAdd() throws Exception {

		Map<String, String> response = new HashMap<>();
		response.put(ERROR, USER_ALREADY_ADDED);

		when(userService.addConnection(eq(email), any(ConnexionForm.class))).thenReturn(response);

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", "user@test.com")).andDo(print())
				.andExpect(flash().attribute(ERROR, response.get(ERROR))).andExpect(status().isFound())
				.andExpect(status().isFound()).andExpect(view().name(REDIR_USER_CONNECTION));
	}

	@Test
	 void testPostConnexionErrorFormat() throws Exception {

		mockMvc.perform(post("/user/connexion").session(mockSession).param("email", "errorFormatEmail")).andDo(print())
				.andExpect(status().isOk()).andExpect(view().name(CONNECTION_CONNECTION));
	}

}
