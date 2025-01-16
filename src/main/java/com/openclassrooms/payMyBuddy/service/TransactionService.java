package com.openclassrooms.payMyBuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;

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
	 * Adds a new transaction to the database.
	 * 
	 * @param transaction The transaction to add.
	 */
	public void addTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}
}
