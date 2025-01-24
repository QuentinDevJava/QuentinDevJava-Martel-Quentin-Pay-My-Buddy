package com.openclassrooms.payMyBuddy.web.controller;

import static com.openclassrooms.payMyBuddy.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.REDIR_TRANSACTION;
import static com.openclassrooms.payMyBuddy.constants.UrlConstants.TRANSACTION;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.model.User;
import com.openclassrooms.payMyBuddy.service.FlashMessageHandler;
import com.openclassrooms.payMyBuddy.service.TransactionService;
import com.openclassrooms.payMyBuddy.service.UserService;
import com.openclassrooms.payMyBuddy.web.form.TransactionFrom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller responsible for managing transactions between users.
 * Handles displaying the transaction page, creating new transactions, and providing error handling.
 */
@Controller
@RequestMapping({ "/transaction", "", "/" })
@Slf4j
@RequiredArgsConstructor
public class TransactionController {

	/** The transaction service. */
	private final TransactionService transactionService;
	
	/** The user service. */
	private final UserService userService;
	
	/** The flash attribute. */
	private final FlashMessageHandler flashAttribute;

	  /**
	  * Displays the transaction page, showing the current user's connections and past transactions.
	  * 
	  * @param model The model used to pass data to the view.
	  * @param request The HTTP request containing the session information.
	  * @return The name of the transaction view.
	  */
	@GetMapping
	public String transactionForm(Model model, HttpServletRequest request) {
		log.info("Loading the transaction page");
		HttpSession session = request.getSession();
		User user = userService.getUserByEmail(session.getAttribute(SESSION_ATTRIBUTE).toString());
		Set<User> userSet = user.getConnections();

		TransactionFrom transactionFrom = new TransactionFrom();
		List<Transaction> transactions = transactionService.getTransactionsBySenderId(user.getId());
		List<TransactionFrom> transactionFroms = transactions.stream().map(TransactionFrom::new).toList();

		model.addAttribute("transactionFrom", transactionFrom);
		model.addAttribute("ListoftransactionFrom", transactionFroms);
		model.addAttribute("userset", userSet);
		return TRANSACTION;
	}
	
    /**
     * Handles the creation of a new transaction. If there are validation errors, the errors are shown; 
     * otherwise, the transaction is saved and a success message is displayed.
     * 
     * @param request The HTTP request containing session information.
     * @param transactionFrom The transaction form data submitted by the user.
     * @param result The result of validation on the form data.
     * @param redirAttrs Attributes used to pass messages between redirects.
     * @return A redirect to the transaction page, with either an error or success message.
     */
	@PostMapping
	public String createTransaction(HttpServletRequest request, @Valid @ModelAttribute TransactionFrom transactionFrom,
			BindingResult result, RedirectAttributes redirAttrs) {

		StringBuilder builder = new StringBuilder();

		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				builder.append(error.getDefaultMessage()).append("<br>");
			}
			log.debug("Transaction error : {}", builder.toString());
			flashAttribute.errorMessage(redirAttrs, builder.toString());
		} else {
			HttpSession session = request.getSession();
			int userId = userService.getUserByEmail(session.getAttribute(SESSION_ATTRIBUTE).toString()).getId();

			Transaction transaction = new Transaction();
			transaction.setReceiver(userService.getUserById(transactionFrom.getReceiverId()));
			transaction.setSender(userService.getUserById(userId));
			transaction.setDescription(transactionFrom.getDescription());
			transaction.setAmount(transactionFrom.getAmount());
			transactionService.addTransaction(transaction);
			log.info("Transaction successfully added");
			flashAttribute.successMessage(redirAttrs,
					"Le transfert vers " + transaction.getReceiver().getUsername() + " a été effectué avec succès.");
		}
		return REDIR_TRANSACTION;
	}
}
