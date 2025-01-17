package com.openclassrooms.payMyBuddy.constants;

public final class AppConstants {

	private AppConstants() {
		super();
	}

	public static final String SESSION_ATTRIBUTE = "username";

	public static final String ERROR = "error";
	public static final String SUCCESS = "success";

	public static final String USERNAME_OR_EMAIL_IS_USE = "Nom d'utilisateur ou mail déjà utilisé.";

	public static final String PASSWORD_SUCCESS = "Mot de passe mise à jour avec succès.";
	public static final String PASSWORD_NOT_MATCH = "Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.";
	public static final String OLD_PASSWORD_FALSE = "L'ancien mot de passe est incorrect.";

	public static final String UNKNOW_USER = "Utilisateur inconnu.";
	public static final String USER_CANNOT_CONNECT_TO_THEMSELF = "L'utilisateur ne peut pas établir une connexion avec lui-même.";
	public static final String USER_ALREADY_ADDED = "Utilisateur déjà ajouté.";

}
