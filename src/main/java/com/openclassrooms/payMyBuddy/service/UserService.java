package com.openclassrooms.payMyBuddy.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;

/**
 * Service that handles operations related to users. This service provides
 * methods to retrieve users from the database through the
 * {@link UserRepository}.
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	/**
	 * Retrieves all users.
	 * 
	 * @return An iterable containing all {@link User} objects from the database.
	 */
	public Iterable<User> getUsers() {
		return userRepository.findAll();
	}

	/**
	 * Retrieves a user by their ID.
	 * 
	 * @param id The ID of the user to retrieve.
	 * @return An {@link Optional} containing the {@link User} if found, otherwise
	 *         {@link Optional#empty()}.
	 */
	public Optional<User> getUserById(int id) {
		return userRepository.findById(id);
	}

	/**
	 * Retrieves a list of users by their username.
	 * 
	 * @param username The username to search for.
	 * @return An iterable containing all {@link User} objects with the specified
	 *         username.
	 */
	public Iterable<User> getUsersByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * Retrieves a user by their email address.
	 * 
	 * @param email The email address of the user to search for.
	 * @return An {@link Optional} containing the {@link User} if found, otherwise
	 *         {@link Optional#empty()}.
	 */
	public Optional<User> getUsersByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User addUser(User user) {
		return userRepository.save(user);
	}

}
