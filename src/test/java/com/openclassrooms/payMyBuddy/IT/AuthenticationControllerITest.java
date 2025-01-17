package com.openclassrooms.payMyBuddy.IT;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Test
	public void testGetLogin() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("user/login"));
	}

	@Test
	public void testPostLogin() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "TestPassword1!";

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setUsername(username);
		mockUser.setPassword(password);

		when(userService.getUserByEmailOrUsername(email, email)).thenReturn(mockUser);
		when(userService.identifierAndPasswordIsValide(anyString(), anyString())).thenReturn(true);
		mockMvc.perform(post("/login").param("identifier", email).param("password", password))
				.andExpect(view().name("redirect:/transaction"));
	}

	@Test
	public void testPostLoginErrorUserNull() throws Exception {
		String email = "Test@test.fr";
		String password = "TestPassword1!";

		when(userService.getUserByEmailOrUsername(email, email)).thenReturn(null);
		mockMvc.perform(post("/login").param("identifier", email).param("password", password))
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	public void testPostLoginErrorPassword() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "TestPassword1!";

		User mockUser = new User();
		mockUser.setEmail(email);
		mockUser.setUsername(username);
		mockUser.setPassword(password);

		when(userService.getUserByEmailOrUsername(email, email)).thenReturn(mockUser);
		when(userService.identifierAndPasswordIsValide(anyString(), anyString())).thenReturn(false);
		mockMvc.perform(post("/login").param("identifier", email).param("password", password))
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	public void testGetRegistration() throws Exception {
		mockMvc.perform(get("/registration")).andExpect(status().isOk()).andExpect(view().name("user/registration"));
	}

	@Test
	public void testPostRegistration() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "TestPassword1!";

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.setUsername(username);
		registrationForm.setEmail(email);
		registrationForm.setPassword(password);

		doNothing().when(userService).addUser(registrationForm);

		mockMvc.perform(
				post("/registration").param("username", username).param("email", email).param("password", password))
				.andExpect(view().name("redirect:/login"));
	}

	@Test
	public void testPostRegistrationError() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "Test";

		RegistrationForm registrationForm = new RegistrationForm();
		registrationForm.setUsername(username);
		registrationForm.setEmail(email);
		registrationForm.setPassword(password);

		doNothing().when(userService).addUser(registrationForm);

		mockMvc.perform(
				post("/registration").param("username", username).param("email", email).param("password", password))
				.andExpect(view().name("user/registration"));
	}

	@Test
	public void testLogout() throws Exception {
		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute(SESSION_ATTRIBUTE, "test@test.com");
		mockMvc.perform(get("/logout").session(mockSession)).andExpect(status().isFound())
				.andExpect(view().name("redirect:/login"));
	}

}
