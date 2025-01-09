package com.openclassrooms.payMyBuddy.IT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.TransactionRepository;
import com.openclassrooms.payMyBuddy.repository.UserRepository;
import com.openclassrooms.payMyBuddy.service.TrippleDes;

import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("prod")
public class DatabaseTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Test
	public void testDatabaseConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		assertNotNull(connection);
		assertTrue(connection.isValid(1));
		connection.close();
	}

	@Test
	public void testTablesExist() {
		assertTrue(userRepository.count() >= 0);
		assertTrue(transactionRepository.count() >= 0);
	}

	@Test
	@Transactional
	public void testUserCrudOperations() throws Exception {
		User user = new User();
		user.setUsername("Test");
		user.setEmail("Test@test.fr");
		user.setPassword("Test1!78");

		user = userRepository.save(user);

		assertNotNull(user.getId());
		assertEquals("Test", user.getUsername());

		User foundUser = userRepository.findById(user.getId()).orElse(null);
		assertNotNull(foundUser);
		assertEquals("Test@test.fr", foundUser.getEmail());

		foundUser.setPassword("Test1!78@");
		userRepository.save(foundUser);

		User updatedProduct = userRepository.findById(user.getId()).orElse(null);
		assertNotNull(updatedProduct);
		TrippleDes td = new TrippleDes();
		String mdp = td.encrypt("Test1!78@");
		assertEquals(mdp, updatedProduct.getPassword());

		userRepository.deleteById(user.getId());

		assertFalse(userRepository.existsById(user.getId()));
	}

	@Test
	@Transactional
	public void testTransactionCrudOperations() throws Exception {
		User user = new User();
		user.setUsername("Test");
		user.setEmail("Test@test.fr");
		user.setPassword("Test1!78");
		User user2 = new User();
		user2.setUsername("Test2");
		user2.setEmail("Test2@test.fr");
		user2.setPassword("Test1!78");

		Transaction transaction = new Transaction();
		transaction.setDescription("Test");
		transaction.setAmount(10);
		transaction.setSender(user);
		transaction.setReceiver(user2);

		transaction = transactionRepository.save(transaction);

		assertNotNull(transaction.getId());
		assertEquals("Test", transaction.getDescription());

		Transaction foundUser = transactionRepository.findById(transaction.getId()).orElse(null);
		assertNotNull(foundUser);
		assertEquals(10, foundUser.getAmount());

		transactionRepository.deleteById(transaction.getId());

		assertFalse(transactionRepository.existsById(transaction.getId()));

		userRepository.deleteById(user.getId());
		userRepository.deleteById(user2.getId());

	}
}
