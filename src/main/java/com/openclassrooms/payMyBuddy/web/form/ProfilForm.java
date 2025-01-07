package com.openclassrooms.payMyBuddy.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfilForm {

	@NotBlank
	@Size(min = 2, max = 30, message = "La taille du nom d'utilisateur doit être comprise entre 2 et 30")
	private String username;

	@NotBlank
	@Email
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Le format de l'adresse mail doit être valide avec un domaine et une extension (ex: example@domain.com)")
	private String email;

}
