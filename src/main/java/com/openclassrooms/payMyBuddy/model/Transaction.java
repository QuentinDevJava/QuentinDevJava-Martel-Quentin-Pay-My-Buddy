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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
@Setter
@Getter
@ToString
@EqualsAndHashCode
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
	private int amount;

	// TODO javadoc
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	// TODO javadoc
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;
}
