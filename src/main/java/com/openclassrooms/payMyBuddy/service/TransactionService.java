package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import com.openclassrooms.payMyBuddy.web.form.TransactionFrom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing transactions.
 * 
 * This service provides methods for retrieving transactions sent by a user
 * and for adding new transactions.
 * 
 * <p><b>Methods:</b></p>
 * <ul>
 *   <li><b>{@link #getTransactionsBySenderId} :</b> Retrieves all transactions sent by a user, identified by their ID.</li>
 *   <li><b>{@link #addTransaction} :</b> Adds a new transaction.</li>
 * </ul>
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;
	private final UserService userService;

	/**
	 * Retrieves all transactions sent by a user based on their ID.
	 * 
	 * @param id The ID of the user who sent the transactions.
	 * @return A list of transactions sent by the user.
	 */
	public List<Transaction> getTransactionsBySenderId(int id) {
		List<Transaction> senderTransaction = new ArrayList<>();
		transactionRepository.findBySenderId(id).forEach(senderTransaction::add);
		return senderTransaction;
	}


	public Transaction addTransaction(TransactionFrom transactionForm, String identifier) {
		User user = userService.getUserByEmailOrUsername(identifier);
		Transaction transaction = buildTransaction(transactionForm, user);
		Transaction createdTransaction = transactionRepository.save(transaction);
		log.info("Transaction successfully added");
		return createdTransaction;
	}

	private Transaction buildTransaction(TransactionFrom transactionForm, User user) {
		User receiver = userService.getUserById(transactionForm.getReceiverId());
		Transaction transaction = new Transaction();
		transaction.setSender(user);
		transaction.setReceiver(receiver);
		transaction.setDescription(transactionForm.getDescription());
		transaction.setAmount(transactionForm.getAmount());
		return transaction;
	}
}
