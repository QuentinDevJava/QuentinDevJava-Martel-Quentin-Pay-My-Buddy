package com.openclassrooms.payMyBuddy.IT;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

//TODO je doit moker qu'il y as une session ouvert pour pouvoir faire les test controller
	@Test
	public void testProfil() throws Exception {
		mockMvc.perform(get("/profile")).andDo(print()).andExpect(status().isFound())
				.andExpect(view().name("user/profile"));
	}

}
