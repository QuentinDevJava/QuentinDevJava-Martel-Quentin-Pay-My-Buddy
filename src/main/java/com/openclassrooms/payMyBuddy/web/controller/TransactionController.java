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
		if (session == null || session.getAttribute("username") == null) {
			log.info("SESSION is null");
			return "redirect:/login";
		}
		String username = session.getAttribute("username").toString();
		log.info("USERNAME retrieved from current session: {}", username);

		User user = userService.getUserByEmail(session.getAttribute("username").toString());
		user = userService.getUserById(user.getId());
		Set<User> userSet = user.getConnections();

		TransactionFrom transactionDTO = new TransactionFrom();

		List<Transaction> transactions = transactionService.getTransactionsBySenderId(user.getId());
		List<TransactionFrom> transactionDTOs = transactions.stream().map(TransactionFrom::new).toList();

		model.addAttribute("transactionDTO", transactionDTO);
		model.addAttribute("listTransactionDTO", transactionDTOs);
		model.addAttribute("userset", userSet);

		return "transaction/transaction";

	}

	@PostMapping
	public String createTransaction(HttpServletRequest request, @Valid @ModelAttribute TransactionFrom transactionFrom,
			BindingResult result) {
		HttpSession session = request.getSession();

		Transaction transaction = new Transaction();
		int userId = userService.getUserByEmail(session.getAttribute("username").toString()).getId();
		transaction.setReceiver(userService.getUserById(transactionFrom.getReceiverId()));
		transaction.setSender(userService.getUserById(userId));
		transaction.setDescription(transactionFrom.getDescription());
		transaction.setAmount(transactionFrom.getAmount());
		transactionService.saveTransaction(transaction);
		return "redirect:/transaction";
	}
}
