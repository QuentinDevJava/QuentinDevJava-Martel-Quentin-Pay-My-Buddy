package com.openclassrooms.payMyBuddy.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.openclassrooms.payMyBuddy.DTO.TransactionDTO;
import com.openclassrooms.payMyBuddy.model.Transaction;
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

	@GetMapping
	public String transaction(Model model) {

		TransactionDTO transactionDTO = new TransactionDTO();
		model.addAttribute("transactionDTO", transactionDTO);

		List<Transaction> transactions = transactionService.getTransactions();
		List<TransactionDTO> transactionDTOs = transactions.stream().map(t -> new TransactionDTO(t)).toList();
		model.addAttribute("listTransactionDTO", transactionDTOs);

		return "transaction/transaction";

	}

	@PostMapping()
	public String createTransaction(@Valid @ModelAttribute TransactionDTO transactionDTO, BindingResult result) {
		Transaction transaction = new Transaction();
		transaction.setReceiver(userService.getUserById(transactionDTO.getReceiverId()));
		transaction.setSender(userService.getUserById(transactionDTO.getSenderId())); // TODO passer l'id de user
																						// connecté
		transaction.setDescription(transactionDTO.getDescription());
		transaction.setAmount(transactionDTO.getAmount());
		transactionService.saveTransaction(transaction);
		return "redirect:/";
	}
}
