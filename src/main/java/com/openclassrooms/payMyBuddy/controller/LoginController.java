package com.openclassrooms.payMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.DTO.UserDTO;
import com.openclassrooms.payMyBuddy.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/login")
@Slf4j
@RequiredArgsConstructor
public class LoginController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String connexion(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("userDTO", userDTO);
		return "user/login";

	}

	@PostMapping()
	public String connextion(@Valid @ModelAttribute UserDTO userDTO, BindingResult result) {
		if (userService.existsByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword())) {
			return "redirect:/"; // pour voir le resultat en fonctionement go a la page des transactions
		} else {
			result.addError(
					new FieldError("userDTO", "email", userDTO.getEmail(), false, null, null, "Account not exist"));
			return "redirect:/user/create"; // page de creation de compte
		}
	}
}
