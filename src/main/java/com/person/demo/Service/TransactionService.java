package com.person.demo.Service;

import com.person.demo.Exception.AppException;
import com.person.demo.Repository.PersonRepository;
import com.person.demo.Repository.TransactionRepository;
import com.person.demo.pojo.Person;
import com.person.demo.pojo.Transaction;
//import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionService {

    //private static final Logger LOG = Logger.getLogger(TransactionService.class);

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private MyUserDetailsService userService;

    public List<Transaction> getAllTransaction() throws AppException{
        //LOG.info("Getting all transactions");
        try {
            Person person = personRepository.findPersonByPersonEmail(userService.userName);
            return getAllByPersonId(person.getPersonId());
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error getting all transactions", e);
            throw new AppException("Error getting all transactions");
        }
    }

    public List<Transaction> getAllByPersonId(int personId) throws AppException{
       // LOG.info("Getting all transactions for person with id = "+personId);
        try {
            Person person = personRepository.getOne(personId);
            return transactionRepository.findAllByPerson(person);
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error getting all transactions by person with id = "+personId, e);
            throw new AppException("Error getting all transactions by person with id = "+personId);
        }
    }

    public Transaction createTransaction(Transaction transaction) throws AppException{
        //LOG.info("Creating new transaction");
        try {
            Person person = personRepository.findPersonByPersonEmail(userService.userName);
            transaction.setPerson(person);
            return transactionRepository.save(transaction);
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error creating new transaction", e);
            throw new AppException("Error creating transaction");
        }
    }

    public ResponseEntity<String> updateTransaction(Transaction transaction) throws AppException{
        //LOG.info("Updating Transaction");
        try {
            Person current_person = personRepository.findPersonByPersonEmail(userService.userName);
            Person owner = transactionRepository.getOne(transaction.getId()).getPerson();
            if(owner.equals(current_person)) {
                transaction.setPerson(owner);
                transactionRepository.save(transaction);
                return ResponseEntity.ok("Transaction Updated successfully");
            }
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Permission denied to update the transaction");
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error updating transaction with id="+transaction.getId(), e);
            throw new AppException("Error updating transaction with id="+transaction.getId());
        }
    }

    public ResponseEntity<String> deleteTransaction(Long transactionId) throws AppException{
       // LOG.info("Deleting Transaction with id= "+ transactionId);
        try {
            Person current_person = personRepository.findPersonByPersonEmail(userService.userName);
            Person owner = transactionRepository.findOneById(transactionId).getPerson();
            if(owner.equals(current_person)) {
                transactionRepository.delete(transactionRepository.findOneById(transactionId));
                return ResponseEntity.ok("Transaction deleted successfully");
            }
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Permission denied to delete the transaction");
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error deleting transaction with id="+transactionId, e);
            throw new AppException("Error deleting transaction with id="+transactionId);
        }
    }

}
