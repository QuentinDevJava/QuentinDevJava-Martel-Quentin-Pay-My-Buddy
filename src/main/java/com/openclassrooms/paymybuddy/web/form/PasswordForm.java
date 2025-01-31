package com.openclassrooms.paymybuddy.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Form used for password change.
 *
 * <p>
 * <b>Attributes:</b>
 * </p>
 * <ul>
 * <li><b>{@link #oldPassword}:</b> The user's current password.</li>
 * <li><b>{@link #password}:</b> The new password, which must meet specific
 * complexity rules.</li>
 * <li><b>{@link #passwordConfirmation}:</b> The confirmation of the new
 * password.</li>
 * </ul>
 * 
 * <p>
 * <b>Validations:</b>
 * </p>
 * <ul>
 * <li>{@link #oldPassword} is required and cannot be blank.</li>
 * <li>{@link #password} must follow a specific pattern: between 8 and 32
 * characters, with at least one uppercase letter, one lowercase letter, one
 * digit, and one special character.</li>
 * <li>{@link #passwordConfirmation} must match {@link #password}.</li>
 * </ul>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PasswordForm {

	/**
	 * The user's current password.
	 */
	@NotBlank
	private String oldPassword;

	/**
	 * The new password for the user.
	 */
	@NotBlank
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,32}$", message = "Le mot de passe doit contenir entre 8 et 32 caractères, avec au moins une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial.")
	private String password;

	/**
	 * The confirmation of the new password.
	 */
	@NotBlank
	private String passwordConfirmation;
}
