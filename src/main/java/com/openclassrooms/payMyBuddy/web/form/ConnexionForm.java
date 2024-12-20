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

public class ConnexionForm {

	@NotBlank
	private String email;
}
