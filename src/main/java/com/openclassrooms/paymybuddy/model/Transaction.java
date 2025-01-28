package com.openclassrooms.paymybuddy.model;

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
 * This class maps to the "pmb_transaction" table in the database.
 * 
 * <p>
 * <b>Attributes:</b>
 * </p>
 * <ul>
 * <li><b>{@link #id}:</b> Unique identifier for the transaction.</li>
 * <li><b>{@link #description}:</b> Description of the transaction.</li>
 * <li><b>{@link #amount}:</b> Amount of money transferred.</li>
 * <li><b>{@link #sender}:</b> User sending the money.</li>
 * <li><b>{@link #receiver}:</b> User receiving the money.</li>
 * </ul>
 */
@Entity
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Table(name = "pmb_transaction")
public class Transaction {

	/**
	 * The unique identifier of the transaction.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * A description of the transaction.
	 */
	@Column(name = "description")
	private String description;

	/**
	 * The amount of money transferred in the transaction.
	 */
	@Column(name = "amount")
	private double amount;

	/**
	 * User sending the money.
	 */
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id")
	private User sender;

	/**
	 * User receiving the money.
	 */
	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_id")
	private User receiver;
}
