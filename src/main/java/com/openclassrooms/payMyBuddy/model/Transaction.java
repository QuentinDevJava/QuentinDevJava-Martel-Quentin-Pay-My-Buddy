package com.openclassrooms.payMyBuddy.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Represents a financial transaction between two users.
 * 
 * This class maps to the "transaction" table in the database and stores
 * the details of a financial transaction, including the sender, receiver,
 * amount, and a description of the transaction.
 * 
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #id}:</b> The unique identifier for the transaction (auto-generated).</li>
 *   <li><b>{@link #senderId}:</b> The ID of the user who is sending the money.</li>
 *   <li><b>{@link #receiverId}:</b> The ID of the user who is receiving the money.</li>
 *   <li><b>{@link #description}:</b> A description or note for the transaction.</li>
 *   <li><b>{@link #amount}:</b> The amount of money being transferred in the transaction.</li>
 * </ul>
 */
@Entity
@Table(name = "transaction")
public class Transaction {

	/**
	 * The unique identifier of the transaction.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * A description or note for the transaction.
	 */
	@Column(name = "description")
	private String description;

	/**
	 * The amount of money being transferred in the transaction.
	 */
	@Column(name = "amount")
	private double amount;

	// TODO implementer les relations avec user pour sender_id et receiver_id
	// TODO javadoc
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	// TODO javadoc
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;

	/**
	 * Returns the unique identifier of the transaction.
	 * 
	 * @return The transaction's unique identifier.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the unique identifier of the transaction.
	 * 
	 * @param id The unique identifier to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the user who is sending the money.
	 * 
	 * @return The sender's user ID.
	 */
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	/**
	 * Returns the user who is receiving the money.
	 * 
	 * @return The receiver's user ID.
	 */
	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	/**
	 * Returns the description or note for the transaction.
	 * 
	 * @return The description of the transaction.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description or note for the transaction.
	 * 
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the amount of money being transferred in the transaction.
	 * 
	 * @return The amount of the transaction.
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * Sets the amount of money being transferred in the transaction.
	 * 
	 * @param amount The amount to set for the transaction.
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

}
