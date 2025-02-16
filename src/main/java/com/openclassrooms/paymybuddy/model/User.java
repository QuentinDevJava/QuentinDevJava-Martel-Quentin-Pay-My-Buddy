package com.openclassrooms.paymybuddy.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.openclassrooms.paymybuddy.web.form.RegistrationForm;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a user in the system.
 * 
 * This class maps to the "pmb_user" table in the database and includes the
 * user's personal details, as well as their transactions and connections with
 * other users.
 * 
 * <p>
 * <b>Attributes:</b>
 * </p>
 * <ul>
 * <li><b>{@link #id} :</b> Unique identifier of the user (automatically
 * generated).</li>
 * <li><b>{@link #username} :</b> The user's username used for login and
 * identification.</li>
 * <li><b>{@link #email} :</b> Email address used for communication and
 * authentication.</li>
 * <li><b>{@link #password} :</b> Hashed password used for authentication.</li>
 * </ul>
 * 
 * <p>
 * <b>Relationships:</b>
 * </p>
 * <ul>
 * <li>A user can send and receive {@link Transaction}s through
 * {@link OneToMany} relationships.</li>
 * <li>A user can be connected to multiple other users through a
 * {@link ManyToMany} relationship. These connections are stored in the
 * "pmb_user_connections" table.</li>
 * </ul>
 */

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "pmb_user")
public class User {

	public User(RegistrationForm registrationForm) {
		this.username = registrationForm.getUsername();
		this.email = registrationForm.getEmail();
	}

	/**
	 * The unique identifier of the user.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	/**
	 * The username of the user.
	 */
	@Column(name = "username")
	private String username;

	/**
	 * The email address of the user.
	 */
	@Column(name = "email")
	private String email;

	/**
	 * The password of the user (hashed).
	 */
	@Column(name = "password")
	private String password;

	/**
	 * List of transactions sent by the user.
	 * 
	 * Each transaction represents a money transfer made by this user.
	 */

	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Transaction> sentTransactions;

	/**
	 * List of transactions received by the user.
	 * 
	 * Each transaction represents a money amount received by this user.
	 */
	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Transaction> receivedTransactions;

	/**
	 * The set of users that the current user is connected to.
	 * 
	 * The relationship is managed through the "pmb_user_connections" table. The
	 * connections are fetched lazily to optimize performance. The cascade
	 * operations persist and merge changes in the relationship.
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "pmb_user_connections", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "connection_id"))
	private Set<User> connections = new HashSet<>();

}
