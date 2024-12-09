package com.openclassrooms.payMyBuddy;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class PayMyBuddyApplication implements CommandLineRunner {

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Iterable<Transaction> transactions = transactionService.getTransactions();
		Iterable<User> users = userService.getUsers();
		User johny = new User();
		johny.setUsername("JohnyDoe");
		johny.setEmail("johny@example.com");
		johny.setPassword("hashedpassword123");

		User johnyReturn = userService.addUser(johny);
		log.info(johnyReturn.toString());

		log.debug(
				"-------------------------------------------------------------------------------------------------------------");
		log.debug("Requete des connection entre  utilisateurs @@ManyToMany");
		users.forEach(u -> {
			log.info("----Pour l'utilisateur :"
					+ String.format(" User ID: %d, Username: %s, Email: %s", u.getId(), u.getUsername(), u.getEmail()));

			log.info("- Connecté avec  :");
			u.getConnections().forEach(c -> log.info(String.format("- Username: %s", c.getUsername())));
		});

		log.debug(
				"-------------------------------------------------------------------------------------------------------------");
		log.debug("Requete userService.getUsers() (User findAll) liste des utilisateurs");
		users.forEach(u -> log.info("Id : " + u.getId() + " Nom utilisateur : " + u.getUsername() + " Email : "
				+ u.getEmail() + " Password : " + u.getPassword()));
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");
		log.debug(
				"Requete userService.getUsers() (User findAll) @OneToMany liste des utilisateurs avec la liste des transaction des receivers et senders ");
		users.forEach(u -> {
			log.info("----Pour l'utilisateur :"
					+ String.format(" User ID: %d, Username: %s, Email: %s", u.getId(), u.getUsername(), u.getEmail()));

			log.info("  Transactions envoyées :");
			u.getSentTransactions().forEach(
					t -> log.info(String.format("    - Transaction ID: %d, To: %s, Amount: %.2f, Description: %s",
							t.getId(), t.getReceiver().getUsername(), t.getAmount(), t.getDescription())));

			log.info("  Transactions reçues :");
			u.getReceivedTransactions().forEach(
					t -> log.info(String.format("    - Transaction ID: %d, From: %s, Amount: %.2f, Description: %s",
							t.getId(), t.getSender().getUsername(), t.getAmount(), t.getDescription())));
		});
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

		log.debug("Requete transactionService.getTransactions() (Transaction findAll)");

		transactions.forEach(
				t -> log.info("Envoyer par : " + t.getSender().getId() + " reçu par : " + t.getReceiver().getId()
						+ " Description : " + t.getDescription() + " Montant : " + t.getAmount() + " $"));
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

		log.debug(
				"Requete transactionService.getTransactions() (Transaction findAll) utilisation de @ManyToOne pour recuperer le user puis le username du sender et du receiver ");
		transactions.forEach(t -> log
				.info("Envoyer par : " + t.getSender().getUsername() + " reçu par : " + t.getReceiver().getUsername()
						+ " Description : " + t.getDescription() + " Montant : " + t.getAmount() + " $"));
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

		log.debug(
				"Derived Queries test: Requete UserService.getUsersByUsername(\"JohnDoe\") findByUsername(String username)");
		Iterable<User> userByName = userService.getUsersByUsername("JohnDoe");
		userByName.forEach(u -> log.info("id :" + u.getId() + " Nom : " + u.getUsername() + " Email : " + u.getEmail()
				+ " Password : " + u.getPassword()));
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

		log.debug(
				"Derived Queries test: Requete UserService.getUsersByEmail(\"john@example.com\")  findByEmail(String email)");
		Optional<User> optUserByEmail = userService.getUsersByEmail("john@example.com");
		User userbyEmail = optUserByEmail.get();
		log.info("id :" + userbyEmail.getId() + " Nom : " + userbyEmail.getUsername() + " Email : "
				+ userbyEmail.getEmail() + " Password : " + userbyEmail.getPassword());
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

		log.debug("Derived Queries test: Requete transactionService.getTransactionsBySenderId(3)");
		Iterable<Transaction> transactionsSenderId3 = transactionService.getTransactionsBySenderId(3);
		transactionsSenderId3.forEach(t -> log
				.info("Envoyer par : " + t.getSender().getUsername() + " reçu par : " + t.getReceiver().getUsername()
						+ " Description : " + t.getDescription() + " Montant : " + t.getAmount() + " $"));
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

		log.debug("Derived Queries test: Requete transactionService.getTransactionsByReceiverId(3)");
		Iterable<Transaction> transactionsReceiverId3 = transactionService.getTransactionsByReceiverId(3);
		transactionsReceiverId3.forEach(t -> log
				.info("Envoyer par : " + t.getSender().getUsername() + " reçu par : " + t.getReceiver().getUsername()
						+ " Description : " + t.getDescription() + " Montant : " + t.getAmount() + " $"));
		log.debug(
				"-------------------------------------------------------------------------------------------------------------");

	}

}
