package com.openclassrooms.payMyBuddy.web.controller;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.TransactionFrom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//TODO javadoc

@Controller
@RequestMapping({ "/transaction", "", "/" })
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;
	private final UserService userService;

	@GetMapping
	public String transaction(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();

		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		Set<User> userSet = user.getConnections();

		TransactionFrom transactionFrom = new TransactionFrom();

		List<Transaction> transactions = transactionService.getTransactionsBySenderId(user.getId());
		List<TransactionFrom> transactionFroms = transactions.stream().map(TransactionFrom::new).toList();

		model.addAttribute("transactionFrom", transactionFrom);
		model.addAttribute("ListoftransactionFrom", transactionFroms);
		model.addAttribute("userset", userSet);

		return "transaction/transaction";

	}

	@PostMapping
	public String createTransaction(HttpServletRequest request, @Valid @ModelAttribute TransactionFrom transactionFrom,
			BindingResult result, RedirectAttributes redirAttrs) {

		if (result.hasErrors()) {
			if (result.hasFieldErrors("receiverId")) {
				redirAttrs.addFlashAttribute("emailError", result.getFieldError("receiverId").getDefaultMessage());
			}
			if (result.hasFieldErrors("amount")) {
				redirAttrs.addFlashAttribute("amountError", result.getFieldError("amount").getDefaultMessage());
			}
			return "redirect:/transaction";
		}

		HttpSession session = request.getSession();

		Transaction transaction = new Transaction();

		int userId = userService.getUserByEmail(session.getAttribute("username").toString()).getId();

		transaction.setReceiver(userService.getUserById(transactionFrom.getReceiverId()));
		transaction.setSender(userService.getUserById(userId));
		transaction.setDescription(transactionFrom.getDescription());
		transaction.setAmount(transactionFrom.getAmount());

		transactionService.addTransaction(transaction);

		redirAttrs.addFlashAttribute("success",
				"Le transfert vers " + transaction.getReceiver().getUsername() + " a été effectué avec succès.");
		return "redirect:/transaction";
	}
}
