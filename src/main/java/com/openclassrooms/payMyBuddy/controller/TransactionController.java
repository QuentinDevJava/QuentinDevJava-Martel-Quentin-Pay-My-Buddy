package com.openclassrooms.payMyBuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
//	private final TransactionService transactionServce;
//	private final UserService userService;

//	@GetMapping("/{userId}") // TODO Rrcuperation par l'id de user (option 1)
//	ResponseEntity<List<TransactionDTO>> getTransactionsByUserId(@PathVariable int userId) {
//		log.info("GET request received for /transaction");
//		Iterable<Transaction> transactions = transactionServce.getTransactionsBySenderId(userId);
//		List<TransactionDTO> transactionDtoList = new ArrayList<>();
//		transactions.forEach(transaction -> transactionDtoList.add(new TransactionDTO(transaction)));
//		log.info("Transaction successfully created :", transactionDtoList);
//		return ResponseEntity.ok(transactionDtoList);
//	}
//
//	@GetMapping() // TODO Recuperation par l'objet user de la session en cours (option 2)
//	ResponseEntity<List<TransactionDTO>> getUserTransactions(@RequestBody User user) {
//		log.info("GET request received for /transaction");
//		User userById = userService.getUserById(user.getId());
//		List<TransactionDTO> transactionDtoList = userById.getSentTransactions();
//		log.info("Transaction successfully created");
//		return ResponseEntity.ok(transactionDtoList);
//	}
//
//	@PostMapping
//	public ResponseEntity<String> createTransaction(@Validated @RequestBody Transaction transaction)
//			throws URISyntaxException {
//		// TODO maj le retour du post
//		log.info("POST request received for /transaction, adding transaction: {}", transaction);
//		transactionServce.addTransaction(transaction);
//		log.info("Transaction successfully created: {}", transaction);
//		String str = "/transaction";
//		URI uri = new URI(str);
//		return ResponseEntity.created(uri).body("201");
//
//	}

}
