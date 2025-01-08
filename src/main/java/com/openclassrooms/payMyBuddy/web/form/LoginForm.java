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
public class LoginForm {

	@NotBlank // TODO mise en place d'une validation personnaliser utile ?? ou cela suffit
	private String identifier; // email or username

	@NotBlank
	private String password;

}
