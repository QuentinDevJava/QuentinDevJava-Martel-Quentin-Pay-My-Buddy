package com.openclassrooms.payMyBuddy.service;

import java.util.ArrayList;
import java.util.List;

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

	public List<Transaction> getTransactionsBySenderId(int id) {
		List<Transaction> senderTransaction = new ArrayList<>();
		transactionRepository.findBySenderId(id).forEach(senderTransaction::add);
		return senderTransaction;
	}

	public void addTransaction(Transaction transaction) {
		transactionRepository.save(transaction);
	}
}
