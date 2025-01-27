package com.openclassrooms.paymybuddy.UT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserService;
import com.openclassrooms.paymybuddy.web.form.TransactionForm;

@SpringBootTest
 class TransactionServiceTest {

	@Mock
	private TransactionRepository transactionRepository;

	@InjectMocks
	private TransactionService transactionService;
	
	@Mock
	private	UserService  userService;

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
		String identifier = "test@example.com";
		
        TransactionForm transactionForm = new TransactionForm();
        transactionForm.setReceiverId(2);
        transactionForm.setDescription("TransactionForm 1");
        transactionForm.setAmount(100.0);

        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail(identifier);
        

        when(userService.getUserByEmailOrUsername(identifier)).thenReturn(mockUser);
        
        Transaction savedTransaction = new Transaction();
        savedTransaction.setSender(mockUser);
        savedTransaction.setDescription(transactionForm.getDescription());
        savedTransaction.setAmount(transactionForm.getAmount());
        
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction result = transactionService.addTransaction(transactionForm, identifier);

        assertEquals(mockUser, result.getSender());
        assertEquals(transactionForm.getAmount(), result.getAmount());
        assertEquals(transactionForm.getDescription(), result.getDescription());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
        
    

	
}