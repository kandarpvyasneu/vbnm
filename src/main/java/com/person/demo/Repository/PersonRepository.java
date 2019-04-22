package com.person.demo.Repository;

import com.person.demo.pojo.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findPersonByPersonEmail(String Email);
}
