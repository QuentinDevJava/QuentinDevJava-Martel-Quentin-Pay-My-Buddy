package com.openclassrooms.payMyBuddy.IT;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.web.form.TransactionForm;


@SpringBootTest
@Transactional
class TransactionServiceITest {

    @Autowired
    private TransactionService transactionService;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    void testTransactionService() {
        User user = new User();
        user.setUsername("Test");
        user.setEmail("Test@test.fr");
        user.setPassword("Test1!78");
        user = userRepository.save(user);

        User user2 = new User();
        user2.setUsername("Test2");
        user2.setEmail("Test2@test.fr");
        user2.setPassword("Test1!78");
        user2 = userRepository.save(user2);

        TransactionForm transactionForm = new TransactionForm();
        transactionForm.setDescription("Test");
        transactionForm.setAmount(10);
        transactionForm.setSenderId(user.getId());
        transactionForm.setReceiverId(user2.getId());

        transactionService.addTransaction(transactionForm, user.getUsername());

        List<Transaction> transactions = transactionService.getTransactionsBySenderId(user.getId());
        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals("Test", transactions.get(0).getDescription());
    }
}
