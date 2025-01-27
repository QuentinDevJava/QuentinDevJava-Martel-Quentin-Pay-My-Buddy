package com.openclassrooms.payMyBuddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.paymybuddy.model.Transaction;

/**
 * Repository interface for managing {@link Transaction} entities.
 * 
 * This interface extends {@link CrudRepository} to provide CRUD operations
 * for the {@link Transaction} entity.
 * 
 * <p><b>Methods:</b></p>
 * <ul>
 *   <li><b>{@link #findBySenderId} :</b> Finds all transactions sent by a user, identified by their ID.</li>
 * </ul>
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	/**
	 * Finds all transactions sent by a user based on their ID.
	 * 
	 * @param id The ID of the user who sent the transactions.
	 * @return A collection of transactions sent by the user.
	 */
	Iterable<Transaction> findBySenderId(int id);
}
