package com.openclassrooms.payMyBuddy.web.form;

import com.openclassrooms.payMyBuddy.model.Transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionFrom {

	@NotNull
	private int senderId;

	@NotNull(message = "Veuillez selectionner une relation")
	private int receiverId;

	private String description;

	@NotNull(message = "Le montant ne peut pas être nul.")
	@Min(value = 1, message = "Le montant doit être supérieur ou égal à 1.")
	private double amount;

	private String receiverEmail;

	private String receiverUsername;

	public TransactionFrom(Transaction transaction) {
		this.senderId = transaction.getSender().getId();
		this.receiverId = transaction.getReceiver().getId();
		this.receiverUsername = transaction.getReceiver().getUsername();
		this.receiverEmail = transaction.getReceiver().getEmail();
		this.description = transaction.getDescription();
		this.amount = transaction.getAmount();
	}

}
