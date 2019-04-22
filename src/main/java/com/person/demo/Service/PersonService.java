package com.person.demo.Service;

import com.person.demo.Exception.AppException;
import com.person.demo.Repository.PersonRepository;
import com.person.demo.pojo.Person;
//import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonService {

    //private static final Logger LOG = Logger.getLogger(PersonService.class);

    @Autowired
    private PersonRepository personRepository;

    public List<Person> getAllPerson() throws AppException {
        //LOG.info("Getting all persons");
        try {
            List<Person> personList = personRepository.findAll();
            return personList;
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error getting all persons", e);
            throw new AppException("Error getting all persons");
        }
    }

    public Person createPerson(Person person) throws AppException {
        //LOG.info("Creating new person");
        try {
            List<Person> personList = getAllPerson();
            for (Person person1 : personList) {
                if (person1.getPersonEmail().equals(person.getPersonEmail())) {
                    return null;
                }
                break;
            }
            byte[] bytesEncoded = Base64.encodeBase64(person.getPersonPassword().getBytes());
            String pass = new String(bytesEncoded);
            Person person2 = new Person();
            person2.setPersonEmail(person.getPersonEmail());
            person2.setPersonPassword(pass);
            return personRepository.save(person2);
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error creating person", e);
            throw new AppException("Error creating person");
        }
    }
}
