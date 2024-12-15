package com.openclassrooms.payMyBuddy.web.form;

import com.openclassrooms.payMyBuddy.model.User;

import jakarta.validation.constraints.Email;
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

	@NotEmpty()
	private String username;

	@NotEmpty()
	@Email
	private String email;

	@NotEmpty()
	private String password;

	@NotEmpty
	private String oldPassword;

	@NotEmpty
	private String passwordConfirmation;

	private User connexion;

}
