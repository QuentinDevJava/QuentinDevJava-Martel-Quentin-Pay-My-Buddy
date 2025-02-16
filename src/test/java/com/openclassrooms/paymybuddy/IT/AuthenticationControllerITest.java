package com.openclassrooms.paymybuddy.IT;

import static com.openclassrooms.paymybuddy.constants.AppConstants.ERROR;
import static com.openclassrooms.paymybuddy.constants.AppConstants.LOGIN_ERROR;
import static com.openclassrooms.paymybuddy.constants.AppConstants.REGISTRATION_SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.paymybuddy.constants.AppConstants.SUCCESS;
import static com.openclassrooms.paymybuddy.constants.AppConstants.USERNAME_OR_EMAIL_IS_USE;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.LOGIN;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.LOGOUT;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_LOGIN;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REDIR_REGISTRATION;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.REGISTRATION;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.TRANSACTION_PAGE;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.USER_LOGIN;
import static com.openclassrooms.paymybuddy.constants.UrlConstants.USER_REGISTRATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import com.openclassrooms.paymybuddy.web.form.RegistrationForm;

@SpringBootTest
@AutoConfigureMockMvc
 class AuthenticationControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	private String email = "Test@test.fr";
	private String username = "Test";
	private String password = "TestPassword1!";

	private MockHttpSession mockSession = new MockHttpSession();
	private User mockUser = new User();
	private RegistrationForm registrationForm = new RegistrationForm();

	@BeforeEach
	 void setup() {

		mockSession.setAttribute(SESSION_ATTRIBUTE, "test@test.com");

		mockUser.setEmail(email);
		mockUser.setUsername(username);
		mockUser.setPassword(password);

		registrationForm.setUsername(username);
		registrationForm.setEmail(email);
		registrationForm.setPassword(password);
	}

	@Test
	 void testGetLogin() throws Exception {

		mockMvc.perform(get(LOGIN)).andExpect(status().isOk()).andExpect(view().name(USER_LOGIN));
	}

	@Test
	 void testPostLogin() throws Exception {

		when(userService.getUserByEmailOrUsername(email)).thenReturn(mockUser);
		when(userService.isValidCredentials(anyString(), anyString())).thenReturn(true);

		mockMvc.perform(post(LOGIN).param("identifier", email).param("password", password))
				.andExpect(view().name(TRANSACTION_PAGE));
	}

	@Test
	 void testPostLoginErrorUserNull() throws Exception {

		mockMvc.perform(post(LOGIN).param("identifier", email).param("password", password))
				.andExpect(flash().attribute(ERROR, LOGIN_ERROR)).andExpect(view().name(REDIR_LOGIN));
	}

	@Test
	 void testPostLoginErrorPassword() throws Exception {

		when(userService.getUserByEmailOrUsername(email)).thenReturn(mockUser);
		when(userService.isValidCredentials(anyString(), anyString())).thenReturn(false);

		mockMvc.perform(post(LOGIN).param("identifier", email).param("password", password))
				.andExpect(flash().attribute(ERROR, LOGIN_ERROR)).andExpect(view().name(REDIR_LOGIN));
	}

	@Test
	 void testLogout() throws Exception {

		mockMvc.perform(get(LOGOUT).session(mockSession)).andExpect(status().isFound())
				.andExpect(view().name(REDIR_LOGIN));
	}

	@Test
	 void testGetRegistration() throws Exception {
		mockMvc.perform(get(REGISTRATION)).andExpect(status().isOk()).andExpect(view().name(USER_REGISTRATION));
	}

	@Test
	 void testPostRegistration() throws Exception {

		doNothing().when(userService).addUser(registrationForm);

		mockMvc.perform(
				post(REGISTRATION).param("username", username).param("email", email).param("password", password))
				.andExpect(flash().attribute(SUCCESS, REGISTRATION_SUCCESS)).andExpect(view().name(REDIR_LOGIN));
	}

	@Test
	 void testPostRegistrationError() throws Exception {
		
        doThrow(new IllegalArgumentException(USERNAME_OR_EMAIL_IS_USE))
        .when(userService).addUser(any(RegistrationForm.class));
        
		mockMvc.perform(post(REGISTRATION).param("username", username).param("email", email).param("password", password))
		.andExpect(flash().attribute(ERROR, USERNAME_OR_EMAIL_IS_USE))		
		.andExpect(view().name(REDIR_REGISTRATION));

	}
	@Test
	 void testPostRegistrationFormError() throws Exception {
		
		mockMvc.perform(post(REGISTRATION).param("username", username).param("email", email).param("password", "Test"))
		.andExpect(view().name(USER_REGISTRATION));
	}

}
