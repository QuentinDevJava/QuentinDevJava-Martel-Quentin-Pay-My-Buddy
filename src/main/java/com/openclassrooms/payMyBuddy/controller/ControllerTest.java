package com.openclassrooms.payMyBuddy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;

@Controller
@RequestMapping("/test")
public class ControllerTest {
	@Autowired
	private UserService userService;
	@Autowired
	private TransactionService transactionService;

	@GetMapping
	public String index(Model model) {
		List<User> users = userService.getUsers();
		List<Transaction> transactions = transactionService.getTransactions();

		model.addAttribute("listUser", users);
		model.addAttribute("listTransaction", transactions);

		return "test";
	}
}
