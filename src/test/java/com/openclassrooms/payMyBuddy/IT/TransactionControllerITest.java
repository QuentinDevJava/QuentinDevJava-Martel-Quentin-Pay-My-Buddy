package com.openclassrooms.payMyBuddy.IT;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private TransactionService transactionService;

	@Test
	public void testGetTransaction() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "TestPassword1!";

		User mockUser1 = new User();
		mockUser1.setId(1);
		mockUser1.setEmail(email);
		mockUser1.setUsername(username);
		mockUser1.setPassword(password);

		User mockUser2 = new User();
		mockUser2.setId(2);
		mockUser2.setEmail(email);
		mockUser2.setUsername(username);
		mockUser2.setPassword(password);

		Transaction transaction1 = new Transaction();
		transaction1.setSender(mockUser1);
		transaction1.setReceiver(mockUser2);
		transaction1.setAmount(100);
		transaction1.setDescription("Transaction 1");

		Transaction transaction2 = new Transaction();
		transaction2.setSender(mockUser1);
		transaction2.setReceiver(mockUser2);
		transaction2.setAmount(200);
		transaction2.setDescription("Transaction 2");

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction1);
		transactions.add(transaction2);

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.getUserByEmail(email)).thenReturn(mockUser1);
		when(transactionService.getTransactionsBySenderId(1)).thenReturn(transactions);

		mockMvc.perform(get("/transaction").session(mockSession)).andExpect(status().isOk())
				.andExpect(view().name("transaction/transaction"));
	}

	@Test
	public void testPostTransaction() throws Exception {
		String email = "Test@test.fr";
		String username = "Test";
		String password = "TestPassword1!";

		User mockUser1 = new User();
		mockUser1.setId(1);
		mockUser1.setEmail(email);
		mockUser1.setUsername(username);
		mockUser1.setPassword(password);

		User mockUser2 = new User();
		mockUser2.setId(2);
		mockUser2.setEmail(email);
		mockUser2.setUsername("User2");
		mockUser2.setPassword(password);

		Transaction transaction1 = new Transaction();
		transaction1.setSender(mockUser1);
		transaction1.setReceiver(mockUser2);
		transaction1.setAmount(100);
		transaction1.setDescription("Transaction 1");

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		when(userService.getUserByEmail(email)).thenReturn(mockUser1);
		when(userService.getUserById(2)).thenReturn(mockUser2);

		mockMvc.perform(post("/transaction")
				.session(mockSession).param("receiverId", "2").param("description", "KDO").param("amount", "100"))
				.andExpect(status().isFound()).andDo(print())
				.andExpect(flash().attribute("success",
						"Le transfert vers " + mockUser2.getUsername() + " a été effectué avec succès."))
				.andExpect(view().name("redirect:/transaction"));
	}

	@Test
	public void testPostTransactionEmailError() throws Exception {
		String email = "Test@test.fr";

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		mockMvc.perform(post("/transaction").session(mockSession).param("receiverId", "0").param("description", "KDO")
				.param("amount", "100")).andExpect(status().isFound()).andDo(print())
				.andExpect(flash().attribute("emailError", "Veuillez selectionner une relation"))
				.andExpect(view().name("redirect:/transaction"));
	}

	@Test
	public void testPostTransactionAmoutError() throws Exception {
		String email = "Test@test.fr";

		MockHttpSession mockSession = new MockHttpSession();
		mockSession.setAttribute("username", email);

		mockMvc.perform(post("/transaction")
				.session(mockSession).param("receiverId", "2").param("description", "KDO").param("amount", "0"))
				.andExpect(status().isFound()).andDo(print())
				.andExpect(
						flash().attribute("amountError", "Le montant du transfére doit être au supérieur ou égal à 1."))
				.andExpect(view().name("redirect:/transaction"));
	}
}
