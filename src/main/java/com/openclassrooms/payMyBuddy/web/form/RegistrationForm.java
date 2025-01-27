package com.openclassrooms.paymybuddy.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Form for user registration.
 *
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #username}:</b> The user's username. It must be between 2 and 30 characters long.</li>
 *   <li><b>{@link #email}:</b> The user's email address. It must be a valid email format (e.g., example@domain.com).</li>
 *   <li><b>{@link #password}:</b> The user's password. It must follow a security policy (8-32 characters, uppercase, lowercase, digits, and special characters).</li>
 * </ul>
 *
 * <p><b>Validations:</b></p>
 * <ul>
 *   <li>{@link #username}, {@link #email}, and {@link #password} are required and cannot be blank.</li>
 *   <li>{@link #email} must be a valid email address.</li>
 *   <li>{@link #password} must meet a complexity requirement (8-32 characters, uppercase, lowercase, digits, and special characters).</li>
 * </ul>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RegistrationForm {

	/**
	 * The user's username.
	 */
	@NotBlank
	@Size(min = 2, max = 30, message = "Le nom d'utilisateur doit être comprise entre 2 et 30")
	private String username;

	/**
	 * The user's email address.
	 */
	@NotBlank
	@Email
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Le format de l'adresse mail doit être valide avec un domaine et une extension (ex: example@domain.com)")
	private String email;

	/**
	 * The user's password.
	 */
	@NotBlank
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,32}$", message = "Le mot de passe doit contenir entre 8 et 32 caractères, avec au moins une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial.")
	private String password;
}