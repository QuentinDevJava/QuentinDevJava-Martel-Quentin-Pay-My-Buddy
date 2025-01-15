package com.openclassrooms.payMyBuddy.IT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Disabled
public class UserControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testProfil() throws Exception {

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", "test@test.com");

		mockMvc.perform(get("/profile").session(mockSession)).andExpect(status().isFound())
				.andExpect(view().name("user/profile"));
	}

	@Test
	public void testUpdatePassword() throws Exception {

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", "test@test.com");

		mockMvc.perform(get("/updatePassword").session(mockSession)).andExpect(status().isFound())
				.andExpect(view().name("user/password"));
	}

}
