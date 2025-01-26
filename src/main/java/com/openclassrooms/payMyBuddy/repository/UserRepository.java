package com.openclassrooms.payMyBuddy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openclassrooms.payMyBuddy.model.User;

/**
 * Repository interface for managing {@link User} entities.
 * 
 * This interface extends {@link CrudRepository} to provide CRUD operations
 * for the {@link User} entity.
 * 
 * <p><b>Methods:</b></p>
 * <ul>
 *   <li><b>{@link #findByEmail} :</b> Finds a user by their email address.</li>
 *   <li><b>{@link #existsByUsername} :</b> Checks if a user exists with the given username.</li>
 *   <li><b>{@link #existsByEmail} :</b> Checks if a user exists with the given email address.</li>
 *   <li><b>{@link #existsByEmailAndPasswordOrUsernameAndPassword} :</b> Checks if a user exists with the email and password, or with the username and password.</li>
 *   <li><b>{@link #findByEmailOrUsername} :</b> Finds a user by their email address or username.</li>
 * </ul>
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

	/**
	 * Finds a user by their email address.
	 * 
	 * @param email The email address of the user to search for.
	 * @return An {@link Optional} containing the {@link User} if found, otherwise {@link Optional#empty()}.
	 */
    Optional<User> findByEmail(String email);

	/**
	 * Checks if a user exists with the given username.
	 * 
	 * @param username The username to check.
	 * @return {@code true} if the user exists, {@code false} otherwise.
	 */
    boolean existsByUsername(String username);

	/**
	 * Checks if a user exists with the given email address.
	 * 
	 * @param email The email address to check.
	 * @return {@code true} if the user exists, {@code false} otherwise.
	 */
    boolean existsByEmail(String email);

	/**
	 * Checks if a user exists with the email and password, or with the username and password.
	 * 
	 * @param email The email address of the user.
	 * @param password1 The password associated with the email.
	 * @param username The username.
	 * @param password2 The password associated with the username.
	 * @return {@code true} if the user exists with the email and password, or with the username and password, otherwise {@code false}.
	 */
    boolean existsByEmailAndPasswordOrUsernameAndPassword(String email, String password1, String username,
                                                          String password2);

	/**
	 * Finds a user by their email address or username.
	 * 
	 * @param email The email address of the user.
	 * @param username The username.
	 * @return The found user, or {@code null} if no user is found.
	 */
    User findByEmailOrUsername(String email, String username);

	@Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> byUsernameOrEmail(@Param("identifier") String usernameOrEmail);
}