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
public class PasswordForm {

	@NotBlank
	private String oldPassword;

	@NotBlank
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,32}$", message = "Le mot de passe doit contenir entre 8 et 32 caractères, avec au moins une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial.")
	private String password;

	@NotBlank
	private String passwordConfirmation;

}
