package com.openclassrooms.payMyBuddy.web.form;

import com.openclassrooms.payMyBuddy.model.Transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Form for a transaction to transfer money between two users.
 *
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #senderId}:</b> The ID of the user sending the money.</li>
 *   <li><b>{@link #receiverId}:</b> The ID of the user receiving the money. This field is required and must be greater than or equal to 1.</li>
 *   <li><b>{@link #description}:</b> An optional description for the transaction.</li>
 *   <li><b>{@link #amount}:</b> The amount of the transaction. This field is required and must be greater than or equal to 1.</li>
 *   <li><b>{@link #receiverEmail}:</b> The recipient's email address (optional).</li>
 *   <li><b>{@link #receiverUsername}:</b> The recipient's username (optional).</li>
 * </ul>
 *
 * <p><b>Validations:</b></p>
 * <ul>
 *   <li>{@link #senderId}, {@link #receiverId}, and {@link #amount} are required and must meet specific constraints.</li>
 *   <li>{@link #receiverId} must be greater than or equal to 1.</li>
 *   <li>{@link #amount} must be greater than or equal to 1.</li>
 * </ul>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionFrom {

	/**
	 * The ID of the user sending the money.
	 */
	@NotNull
	private int senderId;

	/**
	 * The ID of the user receiving the money.
	 */
	@NotNull(message = "Please select a relation.")
	@Min(value = 1, message = "Please select a relation.")
	private int receiverId;

	/**
	 * An optional description for the transaction.
	 */
	private String description;

	/**
	 * The amount of the transaction.
	 */
	@NotNull(message = "Amount cannot be null.")
	@Min(value = 1, message = "The transfer amount must be greater than or equal to 1.")
	private double amount;

	/**
	 * The recipient's email address.
	 */
	private String receiverEmail;

	/**
	 * The recipient's username.
	 */
	private String receiverUsername;

	/**
	 * Constructor that initializes a TransactionFrom object from a {@link Transaction} instance.
	 */
	public TransactionFrom(Transaction transaction) {
		this.senderId = transaction.getSender().getId();
		this.receiverId = transaction.getReceiver().getId();
		this.receiverUsername = transaction.getReceiver().getUsername();
		this.receiverEmail = transaction.getReceiver().getEmail();
		this.description = transaction.getDescription();
		this.amount = transaction.getAmount();
	}
}
