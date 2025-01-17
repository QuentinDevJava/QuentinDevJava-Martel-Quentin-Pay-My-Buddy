package com.openclassrooms.payMyBuddy.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Form for updating the user's profile.
 *
 * <p><b>Attributes:</b></p>
 * <ul>
 *   <li><b>{@link #username}:</b> The username of the user.</li>
 *   <li><b>{@link #email}:</b> The user's email address.</li>
 * </ul>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfilForm {

	/**
	 * The username of the user.
	 */
	private String username;

	/**
	 * The email address of the user.
	 */
	private String email;
}
