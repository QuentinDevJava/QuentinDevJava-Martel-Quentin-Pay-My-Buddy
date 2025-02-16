package com.openclassrooms.paymybuddy.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Login form used for user authentication.
 *
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #identifier}:</b> The username or email used for login.</li>
 *   <li><b>{@link #password}:</b> The password associated with the identifier.</li>
 * </ul>
 * 
 * <p><b>Validations:</b></p>
 * <ul>
 *   <li>Both {@link #identifier} and {@link #password} are required fields and must not be blank.</li>
 * </ul>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginForm {

	/**
	 * The username or email used for login.
	 */
	@NotBlank
	private String identifier;

	/**
	 * The password associated with the identifier.
	 */
	@NotBlank
	private String password;
}
