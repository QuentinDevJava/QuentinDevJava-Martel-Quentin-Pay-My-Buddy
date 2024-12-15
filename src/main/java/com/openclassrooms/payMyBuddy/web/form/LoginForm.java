package com.openclassrooms.payMyBuddy.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginForm {

	@NotBlank
	private String username; // this can be username or email

	@NotEmpty
	private String password;

	@NotEmpty
	private String passwordConfirmation;

}
