package com.person.demo.Service;

import com.person.demo.Repository.PersonRepository;
import com.person.demo.pojo.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class MyUserDetailsService implements UserDetailsService {
    public String userName;
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userName = username;
        System.out.println(username);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Person person = personRepository.findPersonByPersonEmail(username);


        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
        if(person != null){
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(passwordEncoder.encode(person.getPersonPassword()));
            builder.roles("USER");

        }

        return builder.build();
    }
}
