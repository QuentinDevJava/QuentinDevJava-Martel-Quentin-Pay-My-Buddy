package com.openclassrooms.payMyBuddy.web.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PasswordForm {

	@NotEmpty()
	private String password;

	@NotEmpty
	private String oldPassword;

	@NotEmpty
	private String passwordConfirmation;

}
