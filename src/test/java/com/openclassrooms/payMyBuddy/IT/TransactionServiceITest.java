package com.openclassrooms.payMyBuddy.IT;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;

@SpringBootTest
 class TransactionServiceITest {

	@Autowired
	private TransactionService transactionService;

	@Test
	 void testTransactionService() throws Exception {
		User user = new User();
		user.setUsername("Test");
		user.setEmail("Test@test.fr");
		user.setPassword("Test1!78");
		User user2 = new User();
		user2.setUsername("Test2");
		user2.setEmail("Test2@test.fr");
		user2.setPassword("Test1!78");

		Transaction transaction = new Transaction();
		transaction.setDescription("Test");
		transaction.setAmount(10);
		transaction.setSender(user);
		transaction.setReceiver(user2);

		transactionService.addTransaction(transaction);

		List<Transaction> transactions = transactionService.getTransactionsBySenderId(user.getId());

		assertEquals("Test", transactions.get(0).getDescription());

	}
}
