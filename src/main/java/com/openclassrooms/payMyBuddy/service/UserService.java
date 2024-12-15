package com.openclassrooms.payMyBuddy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

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

	public User addUser(User user) {
		return userRepository.save(user);
	}

	public void updateUser(User user) {
		user.setId(getUserByEmail(user.getEmail()).getId());
		userRepository.save(user);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void saveUser(User user) {
		userRepository.save(user);
	}

	public boolean existsByEmailAndPassword(String email, String password) {
		return userRepository.existsByEmailAndPassword(email, password);
	}
}
