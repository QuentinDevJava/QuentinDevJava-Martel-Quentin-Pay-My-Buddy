package com.openclassrooms.payMyBuddy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "sender_id")
	private int senderId;

	@Column(name = "receiver_id")
	private int receiverId;

	@Column(name = "description")
	private String description;

	@Column(name = "amount")
	private double amount;

	public Transaction(int id, int senderId, int receiverId, String description, double amount) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.description = description;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSender_id() {
		return senderId;
	}

	public void setSender_id(int sender_id) {
		this.senderId = sender_id;
	}

	public int getReceiver_id() {
		return receiverId;
	}

	public void setReceiver_id(int receiver_id) {
		this.receiverId = receiver_id;
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

}
