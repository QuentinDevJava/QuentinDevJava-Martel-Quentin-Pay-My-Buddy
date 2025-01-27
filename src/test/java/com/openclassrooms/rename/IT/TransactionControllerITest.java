package com.openclassrooms.rename.IT;

import static com.openclassrooms.rename.constants.AppConstants.ERROR;
import static com.openclassrooms.rename.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.rename.constants.AppConstants.SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.rename.model.Transaction;
import com.openclassrooms.rename.model.User;
import com.openclassrooms.rename.service.TransactionService;
import com.openclassrooms.rename.service.UserService;
import com.openclassrooms.rename.web.form.TransactionForm;

@SpringBootTest
@AutoConfigureMockMvc
 class TransactionControllerITest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@MockitoBean
	private TransactionService transactionService;

	private String email1 = "Test@test.fr";
	private String email2 = "Test2@test.fr";
	private String username = "Test";
	private String password = "TestPassword1!";
	private User mockUser1 = new User();
	private User mockUser2 = new User();

	private Transaction transaction1 = new Transaction();
	private Transaction transaction2 = new Transaction();
	private List<Transaction> transactions = new ArrayList<>();
	private MockHttpSession mockSession = new MockHttpSession();

	@BeforeEach
	 void setup()  {
		mockUser1.setId(1);
		mockUser1.setEmail(email1);
		mockUser1.setUsername(username);
		mockUser1.setPassword(password);

		mockUser2.setId(2);
		mockUser2.setEmail(email2);
		mockUser2.setUsername("User2");
		mockUser2.setPassword(password);

		transaction1.setSender(mockUser1);
		transaction1.setReceiver(mockUser2);
		transaction1.setAmount(100);
		transaction1.setDescription("Transaction 1");

		transaction2.setSender(mockUser1);
		transaction2.setReceiver(mockUser2);
		transaction2.setAmount(200);
		transaction2.setDescription("Transaction 2");

		transactions.add(transaction1);
		transactions.add(transaction2);

		mockSession.setAttribute(SESSION_ATTRIBUTE, email1);
	}

	@Test
	 void testGetTransaction() throws Exception {

		when(userService.getUserByEmailOrUsername(email1)).thenReturn(mockUser1);
		when(transactionService.getTransactionsBySenderId(1)).thenReturn(transactions);

		mockMvc.perform(get("/transaction").session(mockSession)).andExpect(status().isOk())
				.andExpect(view().name("transaction/transaction"));
	}

	@Test
	 void testPostTransaction() throws Exception {
		
		when(userService.getUserByEmailOrUsername(email1)).thenReturn(mockUser1);
		when(transactionService.addTransaction(any(TransactionForm.class),anyString())).thenReturn(transaction1);

		mockMvc.perform(post("/transaction")
				.session(mockSession).param("receiverId", "2").param("receiverEmail", email1).param("description", "KDO").param("amount", "100"))
				.andExpect(status().isFound()).andDo(print())
				.andExpect(flash().attribute(SUCCESS,
						"Le transfert vers " + mockUser2.getUsername() + " a été effectué avec succès."))
				.andExpect(view().name("redirect:/transaction"));
	}

	@Test
	 void testPostTransactionEmailError() throws Exception {

		mockMvc.perform(post("/transaction").session(mockSession).param("receiverId", "0").param("description", "KDO")
				.param("amount", "100")).andExpect(status().isFound()).andDo(print())
				.andExpect(flash().attribute(ERROR, "Veuillez selectionner une relation.<br>"))
				.andExpect(view().name("redirect:/transaction"));
	}

	@Test
	 void testPostTransactionAmoutError() throws Exception {

		mockMvc.perform(post("/transaction").session(mockSession).param("receiverId", "2").param("description", "KDO")
				.param("amount", "0")).andExpect(status().isFound()).andDo(print())
				.andExpect(flash().attribute(ERROR, "Le montant du transfére doit être au supérieur ou égal à 1.<br>"))
				.andExpect(view().name("redirect:/transaction"));
	}
}
