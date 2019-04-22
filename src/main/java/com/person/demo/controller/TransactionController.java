package com.person.demo.controller;

import com.person.demo.Exception.AppException;
import com.person.demo.Service.TransactionService;
import com.person.demo.pojo.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/")
    public ResponseEntity<List<Transaction>> getAllTransaction() throws AppException {
        List<Transaction> transactionList = transactionService.getAllTransaction();
        return ResponseEntity.ok(transactionList);
    }

    @PostMapping("/")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) throws AppException {
        Transaction newTransaction = transactionService.createTransaction(transaction);
        return ResponseEntity.ok(newTransaction);
    }

    @PutMapping("/update/{transactionId}")
    public ResponseEntity updateTransaction(@RequestBody Transaction transaction, @PathVariable Long transactionId) throws AppException {
        ResponseEntity response = transactionService.updateTransaction(transaction);
        return response;
    }

    @DeleteMapping("/delete/{transactionId}")
    public ResponseEntity deleteTransaction(@PathVariable Long transactionId) throws AppException {
        ResponseEntity response = transactionService.deleteTransaction(transactionId);
        return response;
    }
}
