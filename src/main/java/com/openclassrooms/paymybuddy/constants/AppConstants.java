package com.openclassrooms.paymybuddy.constants;

/**
 * This class contains common constant values used throughout the application, 
 * such as session keys, success/error messages, and user validation messages. 
 * It helps to avoid hardcoding strings and ensures consistency across the application.
 */
public final class AppConstants {

    /** The session attribute for storing the username or email. */
    public static final String SESSION_ATTRIBUTE = "username";

    /** The key for error messages. */
    public static final String ERROR = "error";
    
    /** The key for success messages. */
    public static final String SUCCESS = "success";

    /** Message when the username or email is already in use. */
    public static final String USERNAME_OR_EMAIL_IS_USE = "Nom d'utilisateur ou mail déjà utilisé.";

    /** Message when the password is successfully updated. */
    public static final String PASSWORD_SUCCESS = "Mot de passe mise à jour avec succès.";
    
    /** Message when the new password and confirmation do not match. */
    public static final String PASSWORD_NOT_MATCH = "Le nouveau mot de passe et la confirmation du mot de passe ne correspondent pas.";
    
    /** Message when the old password is incorrect. */
    public static final String OLD_PASSWORD_FALSE = "L'ancien mot de passe est incorrect.";

    /** Message when the user is unknown. */
    public static final String UNKNOW_USER = "Utilisateur inconnu.";
    
    /** Message when a user tries to connect to themselves. */
    public static final String USER_CANNOT_CONNECT_TO_THEMSELF = "L'utilisateur ne peut pas établir une connexion avec lui-même.";
    
    /** Message when a user has already been added. */
    public static final String USER_ALREADY_ADDED = "Utilisateur déjà ajouté.";

    /** Message  when there is a login error due to incorrect username or password. */
    public static final String LOGIN_ERROR  = "Identifiant ou mot de passe incorrecte.";

    /** Message when the registration success. */
    public static final String REGISTRATION_SUCCESS = "Votre compte utilisateur a été créé avec succès.";

    /** Private constructor to prevent instantiation. */
    private AppConstants() {
        super();
    }
}
