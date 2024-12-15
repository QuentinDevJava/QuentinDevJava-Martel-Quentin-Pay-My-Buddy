package com.openclassrooms.payMyBuddy.controller;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService transactionService;
	private final UserService userService;

	@GetMapping("/{userId}")
	public String transaction(Model model, @PathVariable int userId) {

		User user = userService.getUserById(userId);
		Set<User> userSet = user.getConnections();

		TransactionDTO transactionDTO = new TransactionDTO();

		List<Transaction> transactions = transactionService.getTransactionsBySenderId(userId);

		List<TransactionDTO> transactionDTOs = transactions.stream().map(TransactionDTO::new).toList();

		userSet.forEach(u -> System.out.println(u.getUsername()));

		model.addAttribute("transactionDTO", transactionDTO);
		model.addAttribute("listTransactionDTO", transactionDTOs);
		model.addAttribute("userId", userId);
		model.addAttribute("userset", userSet);

		return "transaction/transaction";

	}

	@PostMapping("/{userId}")
	public String createTransaction(@PathVariable int userId, @Valid @ModelAttribute TransactionDTO transactionDTO,
			BindingResult result) {
		Transaction transaction = new Transaction();

		transaction.setReceiver(userService.getUserById(transactionDTO.getReceiverId()));
		transaction.setSender(userService.getUserById(userId));
		transaction.setDescription(transactionDTO.getDescription());
		transaction.setAmount(transactionDTO.getAmount());
		transactionService.saveTransaction(transaction);
		return "redirect:/transaction/" + userId;
	}
}
