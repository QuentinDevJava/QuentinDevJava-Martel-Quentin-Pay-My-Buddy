package com.openclassrooms.payMyBuddy.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openclassrooms.payMyBuddy.model.User;

/**
 * Repository interface for managing {@link User} entities. This interface
 * extends {@link CrudRepository} to provide CRUD operations for the
 * {@link User} entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * Finds users by their username.
	 * 
	 * @param username The username to search for.
	 * @return An iterable of {@link User} objects with the specified username.
	 */
	public Iterable<User> findByUsername(String username);

	/**
	 * Finds a user by their email address.
	 * 
	 * @param email The email address of the user to search for.
	 * @return An {@link Optional} containing the {@link User} if found, otherwise
	 *         {@link Optional#empty()}.
	 */
	public Optional<User> findByEmail(String email);

	public Optional<Integer> findIdByEmail(String email);

	public boolean existsByUsername(String username);

	public boolean existsByEmail(String email);

	public boolean existsByEmailAndPassword(String email, String password);

	public boolean existsByUsernameAndPassword(String username, String password);

	public boolean existsByEmailOrUsername(String email, String username);

	public boolean existsByEmailAndPasswordOrUsernameAndPassword(String email, String password1, String username,
			String password2);

	public User findByEmailOrUsername(String email, String username);
}