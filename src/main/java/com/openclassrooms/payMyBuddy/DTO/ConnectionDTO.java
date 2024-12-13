package com.openclassrooms.payMyBuddy.DTO;

import java.util.List;
import java.util.Set;

import com.openclassrooms.payMyBuddy.model.User;

public record ConnectionDTO(List<String> emailOfConnectUser) {

	public ConnectionDTO(User user) {
		this(listOfUserEmail(user));

	}

	private static List<String> listOfUserEmail(User user) {
		Set<User> userSet = user.getConnections();
		return userSet.stream().map(User::getEmail).toList();
	}
}
