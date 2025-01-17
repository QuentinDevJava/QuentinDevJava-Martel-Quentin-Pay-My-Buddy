package com.openclassrooms.payMyBuddy.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString

/**
 * Connection form for linking two users via an email.
 *
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #email}:</b> The user's email for the connection.</li>
 * </ul>
 *
 * <p><b>Validations:</b></p>
 * <ul>
 *   <li>The {@link #email} must be a valid email address and must not be blank.</li>
 * </ul>
 */
public class ConnexionForm {

	/**
	 * The user's email address used for the connection.
	 * <p>This field must be a valid email format (e.g., example@domain.com).</p>
	 * <p>This field must not be blank.</p>
	 */
	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Le format de l'''adresse mail doit être valide avec un domaine et une extension (ex: example@domain.com)")
	private String email;
}
