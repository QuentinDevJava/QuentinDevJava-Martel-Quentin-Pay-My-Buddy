package com.openclassrooms.payMyBuddy.web.form;

import java.util.List;
import java.util.Set;

import com.openclassrooms.payMyBuddy.model.User;

public record ConnexionForm(List<String> emailOfConnectUser) {

	public ConnexionForm(User user) {
		this(listOfUserEmail(user));

	}

	private static List<String> listOfUserEmail(User user) {
		Set<User> userSet = user.getConnections();
		return userSet.stream().map(User::getEmail).toList();
	}
}
