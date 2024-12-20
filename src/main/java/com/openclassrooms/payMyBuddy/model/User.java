package com.openclassrooms.payMyBuddy.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.openclassrooms.payMyBuddy.service.TrippleDes;
import com.openclassrooms.payMyBuddy.web.form.RegistrationForm;
import com.openclassrooms.payMyBuddy.web.form.TransactionFrom;

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
import lombok.NoArgsConstructor;

/**
 * Represents a user in the system.
 * 
 * This class maps to the "user" table in the database and includes the user's
 * personal details, as well as their transactions and connections with other
 * users.
 * 
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #id}:</b> The unique identifier for the user (auto-generated).</li>
 *   <li><b>{@link #username}:</b> The username chosen by the user, used for
 *       logging in and identifying the user in the system.</li>
 *   <li><b>{@link #email}:</b> The user's email address, used for communication
 *       and authentication.</li>
 *   <li><b>{@link #password}:</b> The user's password (hashed), used for authentication.</li>
 * </ul>
 * 
 * <p><b>Relationships:</b></p>
 * <ul>
 *   <li>A {@link User} can send and receive {@link Transaction}s, which are mapped
 *       via the {@link OneToMany} relationship.</li>
 *   <li>A {@link User} can have multiple connections to other {@link User}s, which
 *       is managed through a {@link ManyToMany} relationship. These connections are stored
 *       in the "user_connections" table, where the user and their connections are linked.</li>
 * </ul>
 */

@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

	private static final String COMPLEX_PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,32}$";
	private static final Pattern PASSWORD_PATTERN = Pattern.compile(COMPLEX_PASSWORD_REGEX);

	// TODO javadoc
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
	 * The username chosen by the user.
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

	// TODO lien bidirectionnel add javadoc
	@OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Transaction> sentTransactions;

	@OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<Transaction> receivedTransactions;
//-------------------------------------------------------------------

	/**
	 * The set of users that the current user is connected to.
	 * 
	 * The relationship is managed through the "user_connections" table. The
	 * connections are fetched lazily to optimize performance. The cascade
	 * operations persist and merge changes in the relationship.
	 */
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "user_connections", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "connection_id"))
	private Set<User> connections = new HashSet<>();

	/**
	 * Returns the user's ID.
	 * 
	 * @return The user's unique identifier.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the user's ID.
	 * 
	 * @param id The unique identifier to set.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Returns the username of the user.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username of the user.
	 * 
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the user's email address.
	 * 
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the user's email address.
	 * 
	 * @param email The email address to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the user's password.
	 * 
	 * @return The password.
	 * @throws Exception 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the user's password.
	 * 
	 * @param password The password to set.
	 * @throws Exception 
	 */
	public void setPassword(String password) throws Exception {

		if (!PASSWORD_PATTERN.matcher(password).matches()) {
			throw new IllegalArgumentException(
					"Le mot de passe doit contenir entre 8 et 32 caractères, avec au moins une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial");
		}

		TrippleDes td = new TrippleDes();
		this.password = td.encrypt(password);
	}

	/**
	 * Returns the list of transactions where the user is the sender.
	 * 
	 * @return A list of transactions where the user is the sender.
	 */
	public List<TransactionFrom> getSentTransactions() {
		return sentTransactions.stream().map(TransactionFrom::new).toList();
	}

	/**
	 * Returns the list of transactions where the user is the receiver.
	 * 
	 * @return A list of transactions where the user is the receiver.
	 */
	public List<TransactionFrom> getReceivedTransactions() {
		return receivedTransactions.stream().map(TransactionFrom::new).toList();
	}

	/**
	 * Returns the set of users that the current user is connected to.
	 * 
	 * @return A set of connected users.
	 */
	public Set<User> getConnections() {
		return connections;
	}

	/**
	 * Sets the set of users that the current user is connected to.
	 * 
	 * @param connections A set of connected users to set.
	 */
	public void setConnections(Set<User> connections) {
		this.connections = connections;
	}
}
