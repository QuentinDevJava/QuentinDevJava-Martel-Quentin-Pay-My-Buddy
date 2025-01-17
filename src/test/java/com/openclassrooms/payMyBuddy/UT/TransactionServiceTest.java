package com.openclassrooms.payMyBuddy.UT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import com.openclassrooms.payMyBuddy.service.TransactionService;

@SpringBootTest
 class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	 void testGetTransactionsBySenderId() {

		Transaction transaction1 = new Transaction();
		transaction1.setAmount(100);
		transaction1.setDescription("Transaction 1");

		Transaction transaction2 = new Transaction();
		transaction2.setAmount(200);
		transaction2.setDescription("Transaction 2");

		List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

		when(transactionRepository.findBySenderId(1)).thenReturn(transactions);

		List<Transaction> result = transactionService.getTransactionsBySenderId(1);

		assertEquals(2, result.size());
		assertEquals("Transaction 1", result.get(0).getDescription());
		assertEquals("Transaction 2", result.get(1).getDescription());
		verify(transactionRepository, times(1)).findBySenderId(1);
	}

	@Test
	 void testAddTransaction() {
		Transaction transaction = new Transaction();
		transaction.setAmount(50);
		transaction.setDescription("Test Transaction");

		transactionService.addTransaction(transaction);

		verify(transactionRepository, times(1)).save(transaction);
	}
}