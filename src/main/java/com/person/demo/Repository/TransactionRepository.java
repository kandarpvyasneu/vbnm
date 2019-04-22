package com.person.demo.Repository;

import com.person.demo.pojo.Person;
import com.person.demo.pojo.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByPerson(Person person);
    Transaction findOneById(Long transactionId);
}
