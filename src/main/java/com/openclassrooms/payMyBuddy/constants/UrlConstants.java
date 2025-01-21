package com.openclassrooms.payMyBuddy.constants;

/**
 * This class contains constants for all URL paths used in the application.
 * It helps centralize and manage the URLs to avoid duplication and errors.
 */
public final class UrlConstants {

    /** Private constructor to prevent instantiation. */
    private UrlConstants() {
        super();
    }

    /** The login page URL. */
    public static final String LOGIN = "/login";
    
    /** The user login page view. */
    public static final String USER_LOGIN = "user/login";
    
    /** Redirect URL for login. */
    public static final String REDIR_LOGIN = "redirect:/login";

    /** The logout URL. */
    public static final String LOGOUT = "/logout";

    /** The registration page URL. */
    public static final String REGISTRATION = "/registration";
    
    /** The user registration page view. */
    public static final String USER_REGISTRATION = "user/registration";

    /** The transaction page URL. */
    public static final String TRANSACTION = "transaction/transaction";
    
    /** Redirect URL for transactions. */
    public static final String REDIR_TRANSACTION = "redirect:/transaction";

    /** The user management URL. */
    public static final String USER = "/user";

    /** The user profile page URL. */
    public static final String PROFIL = "/profile";
    
    /** The user profile page view. */
    public static final String USER_PROFIL = "user/profile";
    
    /** Redirect URL for user profile. */
    public static final String REDIR_USER_PROFIL = "redirect:/user/profile";
    
    /** The password update page URL. */
    public static final String UPDATE_PASSWORD = "/updatePassword";
    
    /** The user password update page view. */
    public static final String USER_PASSWORD = "user/password";
    
    /** Redirect URL for user password update. */
    public static final String REDIR_USER_UPDATE_PASSWORD = "redirect:/user/updatePassword";

    /** The connection page URL. */
    public static final String CONNECTION = "/connexion";
    
    /** The connection page view. */
    public static final String CONNECTION_CONNECTION = "/connexion/connexion";
    
    /** Redirect URL for user connection. */
    public static final String REDIR_USER_CONNECTION = "redirect:/user/connexion";
}
