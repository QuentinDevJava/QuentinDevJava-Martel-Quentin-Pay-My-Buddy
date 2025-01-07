package com.openclassrooms.payMyBuddy.web.form;

import jakarta.validation.constraints.NotBlank;
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
	private String password;

	@NotBlank
	private String oldPassword;

	@NotBlank
	private String passwordConfirmation;

}
