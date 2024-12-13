package com.openclassrooms.payMyBuddy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.DTO.UserDTO;
import com.openclassrooms.payMyBuddy.service.UserService;

@Controller
@RequestMapping("/connexion")
public class ConnexionController {
	@Autowired
	private UserService userService;

	@GetMapping
	public String index(Model model) {
		UserDTO userDTO = new UserDTO();
		model.addAttribute("userDTO", userDTO);

		return "/connexion/connexion";
	}
}
