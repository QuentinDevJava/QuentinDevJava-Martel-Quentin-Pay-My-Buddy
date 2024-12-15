package com.openclassrooms.payMyBuddy.controller;

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

	@GetMapping("/login")
	public String login(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("userDTO", userDTO);
		return "user/login";

	}

	@PostMapping("/login")
	public String authentication(@Valid @ModelAttribute UserDTO userDTO, BindingResult result) {
		if (userService.existsByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword())) {
			User user = userService.getUserByEmail(userDTO.getEmail());
			int id = user.getId();
			return "redirect:/transaction/" + id;
		} else {
			// result.rejectValue("email", "error.userDTO", "Account does not exist or
			// incorrect credentials.");
			return "user/registration";
		}
	}

	@GetMapping("/registration")
	public String createUser(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("userDTO", userDTO);
		return "user/registration";
	}

	@PostMapping("/registration")
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
			return "redirect:/user/login";
		}

	}

	@GetMapping("/profile/{userId}")
	public String profil(@PathVariable int userId, Model model) {
		UserDTO userDTO = new UserDTO();
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
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername(user.getUsername());
		userDTO.setEmail(user.getEmail());
		userDTO.setPassword(user.getPassword());
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("user", user);
		return "/user/update";
	}

	@PostMapping("/profile/{userId}")
	public String updateUser(Model model, @PathVariable int userId, @Valid @ModelAttribute UserDTO userDTO,
			BindingResult result) {

		User user = userService.getUserById(userId);
		if (user == null) {
			return "redirect:/user/profile/" + userId;
		}
		model.addAttribute("user", user);

		user.setUsername(userDTO.getUsername());
		user.setEmail(userDTO.getEmail()); // TODO verification que l'email n'est pas dans la bd
		user.setPassword(userDTO.getPassword());
		userService.saveUser(user);

		return "redirect:/user/profile/" + userId;

	}

	@GetMapping("/connexion/{userId}")
	public String connexion(@PathVariable int userId, Model model) {

		UserDTO userDTO = new UserDTO();
		model.addAttribute("userDTO", userDTO);
		model.addAttribute("userId", userId);

		return "/connexion/connexion";
	}

	@PostMapping("/connexion/{userId}")
	public String addConnexion(@PathVariable int userId, @Valid @ModelAttribute UserDTO userDTO, BindingResult result) {
		User user = userService.getUserById(userId);
		System.out.println(userDTO.getEmail());
		User connection = userService.getUserByEmail(userDTO.getConnexion().getEmail());
		if (connection != null) {
			user.getConnections().add(connection);
			userService.saveUser(user);
			return "redirect:/transaction/" + userId;
		} else {
			result.rejectValue("email", "error.userDTO", "The user does not exist.");
			return "redirect:/user/connexion/" + userId;
		}
	}
}
