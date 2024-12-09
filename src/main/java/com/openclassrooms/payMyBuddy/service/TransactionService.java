package com.openclassrooms.payMyBuddy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;

@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	public Iterable<Transaction> getTransactions() {
		return transactionRepository.findAll();
	}

	public Optional<Transaction> getTransactionById(int id) {
		return transactionRepository.findById(id);
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
}
