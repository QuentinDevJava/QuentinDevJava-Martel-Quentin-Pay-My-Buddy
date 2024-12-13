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
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/create")
	public String createUser(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("userDTO", userDTO);
		return "user/registration";
	}

	@PostMapping("/create")
	public String createUser(@Valid @ModelAttribute UserDTO userDTO, BindingResult result) {

		if (userService.existsByEmail(userDTO.getEmail())) {
			result.addError(new FieldError("userDTO", "email", userDTO.getEmail(), false, null, null,
					"Email address is already used"));
			return "user/registration";
		} else {
			User user = new User();
			user.setUsername(userDTO.getUsername());
			user.setEmail(userDTO.getEmail());
			user.setPassword(userDTO.getPassword());
			userService.saveUser(user);
			return "redirect:/"; // pour voir le resultat en fonctionement go a la page des transactions
		}

	}

}
