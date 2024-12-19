package com.openclassrooms.payMyBuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public List<User> getUsers() {
		List<User> allUser = new ArrayList<>();
		userRepository.findAll().forEach(allUser::add);
		return allUser;
	}

	public User getUserById(int id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElse(null);
	}

	public User getUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		return user.orElse(null);
	}

	public Integer getUserIdByEmail(String email) {
		Optional<Integer> userId = userRepository.findIdByEmail(email);
		return userId.orElse(null);
	}

	public User addUser(User user) {
		return userRepository.save(user);
	}

	public void updateUser(User user) {
		user.setId(getUserByEmail(user.getEmail()).getId());
		userRepository.save(user);
	}

	public boolean userExistsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public boolean userExistsByEmailAndPassword(String email, String password) {
		return userRepository.existsByEmailAndPassword(email, password);
	}

	public boolean userExistsByUsernameAndPassword(String username, String password) {
		return userRepository.existsByUsernameAndPassword(username, password);
	}

	public boolean userExistsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean identifierIsValide(String email, String password) throws Exception {
		TrippleDes td = new TrippleDes();
		return userRepository.existsByEmailAndPasswordOrUsernameAndPassword(email, td.encrypt(password), email,
				td.encrypt(password));
	}

	public User getUserByEmailOrUsername(String email, String username) {
		return userRepository.findByEmailOrUsername(email, username);
	}

}
