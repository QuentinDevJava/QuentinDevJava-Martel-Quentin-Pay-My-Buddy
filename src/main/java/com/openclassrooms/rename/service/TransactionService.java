package com.openclassrooms.rename.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.rename.model.Transaction;
import com.openclassrooms.rename.model.User;
import com.openclassrooms.rename.repository.TransactionRepository;
import com.openclassrooms.rename.web.form.TransactionForm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
	
	/**
	 * Adds a new transaction based on the provided form data and the user's identifier.
	 * 
	 * @param transactionForm The form containing the details of the transaction to be added.
	 * @param identifier The email or username of the user who is sending the transaction.
	 * @return The newly created transaction.
	 */
	public Transaction addTransaction(TransactionForm transactionForm, String identifier) {
		User user = userService.getUserByEmailOrUsername(identifier);
		Transaction transaction = buildTransaction(transactionForm, user);
		Transaction createdTransaction = transactionRepository.save(transaction);
		log.info("Transaction successfully added");
		return createdTransaction;
	}
	
	/**
	 * Builds a transaction object using the provided form data and user.
	 * 
	 * @param transactionForm The form containing transaction details.
	 * @param user The user sending the transaction.
	 * @return A new Transaction object populated with the provided data.
	 */
	private Transaction buildTransaction(TransactionForm transactionForm, User user) {
		User receiver = userService.getUserById(transactionForm.getReceiverId());
		Transaction transaction = new Transaction();
		transaction.setSender(user);
		transaction.setReceiver(receiver);
		transaction.setDescription(transactionForm.getDescription());
		transaction.setAmount(transactionForm.getAmount());
		return transaction;
	}
}
