package com.openclassrooms.payMyBuddy.DTO;

import com.openclassrooms.payMyBuddy.model.Transaction;

import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TransactionDTO {
	// TODO receiver email - id
	// identifiant personne
	// connecté

	@NotEmpty
	private int senderId;
	@NotEmpty
	private int receiverId;

	private String description;

	@NotEmpty
	private double amount;

	@NotEmpty
	private String receiverEmail;
	@NotEmpty
	private String receiverUsername;

	public TransactionDTO(Transaction transaction) {
		this.senderId = transaction.getSender().getId();
		this.receiverId = transaction.getReceiver().getId();
		this.receiverUsername = transaction.getReceiver().getUsername();
		this.receiverEmail = transaction.getReceiver().getEmail();
		this.description = transaction.getDescription();
		this.amount = transaction.getAmount();
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getReceiverUsername() {
		return receiverUsername;
	}

	public void setReceiverUsername(String receiverUsername) {
		this.receiverUsername = receiverUsername;
	}

}
