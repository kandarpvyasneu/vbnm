package com.person.demo.Service;

import com.person.demo.Exception.AppException;
import com.person.demo.DemoApplication;
import com.person.demo.pojo.Person;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
@Transactional
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    Person person = new Person();

    @Before
    public void setup() {
        person.setPersonEmail("test@gmail.com");
        person.setPersonPassword("test");
    }

    @Test
    public void getAllPersonTest () throws AppException{
        Person person1 = personService.createPerson(person);
        List<Person> personList = personService.getAllPerson();
        assertTrue("More than 1 person was returned", personList.size()>0);
    }

    @Test
    public void createPersonTest() throws AppException{
        Person person1 = personService.createPerson(person);
        assertNotNull("User was Created", person1.getPersonId());
    }
}
