package com.openclassrooms.payMyBuddy.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.web.form.LoginForm;
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


	@GetMapping("/registration")
	public String createUser(Model model) {
		LoginForm userDTO = new LoginForm();
		model.addAttribute("userDTO", userDTO);
		return "user/registration";
	}

	@PostMapping("/registration")
	public String createUser(@Valid @ModelAttribute LoginForm userDTO, BindingResult result) {

		if (userService.existsByEmail(userDTO.getUsername())) {
			result.addError(new FieldError("loginForm", "email", userDTO.getUsername(), false, null, null,
					"Email address is already used"));
			return "user/registration";
		} else {
			User user = new User();
			user.setUsername(userDTO.getUsername());
			user.setPassword(userDTO.getPassword());
			userService.saveUser(user);
			return "redirect:/user/login";
		}

	}

	@GetMapping("/profile/{userId}")
	public String profil(@PathVariable int userId, Model model) {
		LoginForm userDTO = new LoginForm();
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("userId", userId);

		return "user/profile";
	}

	@GetMapping("/update/{userId}") // pour afficher les info avant la maj a faire ??
	public String updateUser(Model model, @PathVariable int userId) {

		User user = userService.getUserById(userId);
		if (user == null) {
			return "redirect:/user/profile/" + userId;
		}
		LoginForm userDTO = new LoginForm();
		userDTO.setUsername(user.getUsername());
//		userDTO.setEmail(user.getEmail());
		userDTO.setPassword(user.getPassword());
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("user", user);
		return "/user/update";
	}

	@PostMapping("/profile/{userId}")
	public String updateUser(Model model, @PathVariable int userId, @Valid @ModelAttribute LoginForm userDTO,
			BindingResult result) {

		User user = userService.getUserById(userId);
		if (user == null) {
			return "redirect:/user/profile/" + userId;
		}
		model.addAttribute("user", user);

		user.setUsername(userDTO.getUsername());
//		user.setEmail(userDTO.getEmail()); // TODO verification que l'email n'est pas dans la bd
		user.setPassword(userDTO.getPassword());
		userService.saveUser(user);

		return "redirect:/user/profile/" + userId;

	}

	@GetMapping("/connexion/{userId}")
	public String connexion(@PathVariable int userId, Model model) {

		LoginForm userDTO = new LoginForm();
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("userId", userId);

		return "/connexion/connexion";
	}


}