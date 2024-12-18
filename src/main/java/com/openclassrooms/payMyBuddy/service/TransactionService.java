package com.openclassrooms.payMyBuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {

	private final TransactionRepository transactionRepository;

	public List<Transaction> getTransactions() {
		List<Transaction> allTransaction = new ArrayList<>();
		transactionRepository.findAll().forEach(allTransaction::add);
		return allTransaction;
	}

	public Transaction getTransactionById(int id) {
		Optional<Transaction> transaction = transactionRepository.findById(id);
		return transaction.orElse(null);
	}

	public List<Transaction> getTransactionsBySenderId(int id) {
		List<Transaction> senderTransaction = new ArrayList<>();
		transactionRepository.findBySenderId(id).forEach(senderTransaction::add);
		return senderTransaction;
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

	public void saveTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}

}
