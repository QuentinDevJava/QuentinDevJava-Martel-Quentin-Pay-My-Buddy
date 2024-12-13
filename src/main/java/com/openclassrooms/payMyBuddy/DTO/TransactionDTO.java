package com.openclassrooms.payMyBuddy.DTO;

import com.openclassrooms.payMyBuddy.model.Transaction;

public record TransactionDTO(int id, String description, double amount, String receiver) { // receiver email - id
																							// identifiant personne
																							// connecté

	public TransactionDTO(Transaction transaction) {
		this(transaction.getId(), transaction.getDescription(), transaction.getAmount(),
				transaction.getReceiver().getUsername());
	}
}
