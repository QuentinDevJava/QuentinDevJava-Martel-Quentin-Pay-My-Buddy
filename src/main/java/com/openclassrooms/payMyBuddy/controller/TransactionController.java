package com.openclassrooms.payMyBuddy.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.payMyBuddy.model.Transaction;
import com.openclassrooms.payMyBuddy.service.TransactionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/transaction")
@Slf4j
@RequiredArgsConstructor
public class TransactionController {
	private final TransactionService transactionServce;

	@PostMapping
	public ResponseEntity<String> createPerson(@Validated @RequestBody Transaction transaction)
			throws URISyntaxException {
		log.info("POST request received for /transaction, adding transaction: {}", transaction);
		transactionServce.addTransaction(transaction);
		log.info("Transaction successfully created: {}", transaction);
		String str = "/transaction";
		URI uri = new URI(str);
		return ResponseEntity.created(uri).body("201");

	}
}
