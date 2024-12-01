package com.openclassrooms.payMyBuddy;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;

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
	public void run(String... args) throws Exception {
		// Test de fonctionement de la connection a la bdd et des services

		Iterable<Transaction> transactions = transactionService.getTransactions();
		Iterable<User> users = userService.getUsers();

		HashMap<Integer, String> tableUsers = new HashMap<>();
		users.forEach(u -> tableUsers.put(u.getId(), u.getUsername()));

		transactions.forEach(t -> log.info(
				"Envoyer par : " + tableUsers.get(t.getSenderId()) + " reçu par : " + tableUsers.get(t.getReceiverId())
						+ " Description : " + t.getDescription() + " Montant : " + t.getAmount() + " $"));

		users.forEach(u -> log.info("Id : " + u.getId() + " Nom utilisateur : " + u.getUsername() + " Email : "
				+ u.getEmail() + " Password : " + u.getPassword()));

		transactions.forEach(t -> log.info("Envoyer par : " + t.getSenderId() + " reçu par : " + t.getReceiverId()
				+ " Description : " + t.getDescription() + " Montant : " + t.getAmount() + " $"));

	}
}
