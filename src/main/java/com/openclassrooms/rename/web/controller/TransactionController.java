package com.openclassrooms.rename.web.controller;
import static com.openclassrooms.rename.constants.AppConstants.SESSION_ATTRIBUTE;
import static com.openclassrooms.rename.constants.UrlConstants.TRANSACTION;
import static com.openclassrooms.rename.constants.UrlConstants.TRANSACTION_PAGE;

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

import com.openclassrooms.rename.model.Transaction;
import com.openclassrooms.rename.model.User;
import com.openclassrooms.rename.service.FlashMessageHandler;
import com.openclassrooms.rename.service.TransactionService;
import com.openclassrooms.rename.service.UserService;
import com.openclassrooms.rename.web.form.TransactionForm;

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
	public String listAndDisplayTransactionsForm(Model model, HttpServletRequest request) {
		log.info("Loading the transaction page");
		HttpSession session = request.getSession();
		User user = userService.getUserByEmailOrUsername(session.getAttribute(SESSION_ATTRIBUTE).toString());

		Set<User> connections = user.getConnections();
		TransactionForm transactionForm = new TransactionForm();
		List<Transaction> transactions = transactionService.getTransactionsBySenderId(user.getId());
		List<TransactionForm> listOfTransactionForm = transactions.stream().map(TransactionForm::new).toList();

		model.addAttribute("transactionForm", transactionForm);
		model.addAttribute("listOfTransactionForm", listOfTransactionForm);
		model.addAttribute("connections", connections);
		return TRANSACTION;
	}

    /**
     * Handles the creation of a new transaction. If there are validation errors, the errors are shown;
     * otherwise, the transaction is saved and a success message is displayed.
     *
     * @param request The HTTP request containing session information.
     * @param transactionForm The transaction form data submitted by the user.
     * @param result The result of validation on the form data.
     * @param redirAttrs Attributes used to pass messages between redirects.
     * @return A redirect to the transaction page, with either an error or success message.
     */
	@PostMapping
	public String createTransaction(HttpServletRequest request, @Valid @ModelAttribute TransactionForm transactionForm,
			BindingResult result, RedirectAttributes redirAttrs) {

		StringBuilder builder = new StringBuilder();

		if (result.hasErrors()) {
			for (FieldError error : result.getFieldErrors()) {
				builder.append(error.getDefaultMessage()).append("<br>");
			}
			log.debug("Transaction error : {}", builder);
			flashAttribute.errorMessage(redirAttrs, builder.toString());
		} else {
			HttpSession session = request.getSession();
			String identifier = session.getAttribute(SESSION_ATTRIBUTE).toString();
			Transaction transaction = transactionService.addTransaction(transactionForm, identifier);
			flashAttribute.successMessage(redirAttrs,
					"Le transfert vers " + transaction.getReceiver().getUsername() + " a été effectué avec succès.");
		}
		return TRANSACTION_PAGE;
	}
}
