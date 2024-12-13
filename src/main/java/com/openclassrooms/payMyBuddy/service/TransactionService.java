package com.openclassrooms.payMyBuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public List<Transaction> getTransactions() {
		List<Transaction> allTransaction = new ArrayList<>();
		transactionRepository.findAll().forEach(allTransaction::add);
		return allTransaction;
	}

	public Transaction getTransactionById(int id) {
		Optional<Transaction> transaction = transactionRepository.findById(id);
		return transaction.orElse(null);
	}

	public Iterable<Transaction> getTransactionsBySenderId(int id) {
		return transactionRepository.findBySenderId(id);
	}

	public Iterable<Transaction> getTransactionsByReceiverId(int id) {
		return transactionRepository.findByReceiverId(id);

	}

	public Transaction addTransaction(Transaction transaction) {
		return transactionRepository.save(transaction);
	}

	public void updateTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

}
