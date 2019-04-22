package com.person.demo.Service;


import com.person.demo.Exception.AppException;
import com.person.demo.DemoApplication;
import com.person.demo.pojo.Person;
import com.person.demo.pojo.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Transactional
public class TransactionServiceTest {

    Transaction transaction = new Transaction();

    TransactionService transactionServiceMock = mock(TransactionService.class);

    @Before
    public void setup() throws AppException{
        transaction.setMerchant("Test merchant");
        transaction.setDescription("Test Description");
        transaction.setCategory("Test Category");
        transaction.setAmount(450);
    }

    @Test
    public void getAllTransactionTest() throws AppException {
        when(transactionServiceMock.createTransaction(transaction)).thenReturn(new Transaction());
        Transaction transactiontest = transactionServiceMock.createTransaction(transaction);
        List<Transaction> transactionList= transactionServiceMock.getAllTransaction();
        assertNotNull("Transactions was returned", transactionList.size()>0);
    }
}
