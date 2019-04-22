package com.person.demo.controller;

import com.amazonaws.services.opsworks.model.App;
import com.person.demo.Exception.AppException;
import com.person.demo.Repository.PersonRepository;
import com.person.demo.Service.AttachmentService;
import com.person.demo.Service.MyUserDetailsService;
import com.person.demo.Service.PersonService;
import com.person.demo.pojo.Person;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/")
public class PersonController {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private PersonService personService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private Environment env;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC = "csye7374";

    private final static Logger logger = LoggerFactory.getLogger(PersonController.class);

    //static final Counter requests = Counter.build().name("requests_total").help("Total number of requests.").register();

    private final Counter myOperationCounterSuccess ;

    public PersonController(MeterRegistry registry)
    {
        myOperationCounterSuccess = io.micrometer.core.instrument.Counter
                .builder("myOperation")
                .description("a description for humans")
                .tags("result", "success")
                .register(registry);
    }
//    @GetMapping("/probe")
//    public ResponseEntity<String> getProbeResult() throws AppException {
//
//        logger.info("-=-=--==INTO THE getProbeResult() method-=-=--=-=-=-");
//        //requests.inc();
//        List<Person> personList = personService.getAllPerson();
//        //boolean isExists = attachmentService.isBucketExists();
//        if (personList.size() > 0 && isExists) {
//            ResponseEntity.status(200);
//            return ResponseEntity.ok("DB connection success !! and Access of S3 bucket success!!");
//        } else {
//            ResponseEntity.status(500);
//            return ResponseEntity.ok("Either DB connection fail or S3 bucket access failed.");
//        }
//
//    }

    @GetMapping("/")
    public ResponseEntity<List<Person>> getAllPerson() throws AppException {
        List<Person> personList = personService.getAllPerson();
        return ResponseEntity.ok(personList);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> createPerson(@Valid @RequestBody Person person) throws AppException {
        Person person1 = personService.createPerson(person);
        if (person1 != null)
            return ResponseEntity.ok("User created");
        else
            return ResponseEntity.ok("User already exists");
    }

    @GetMapping("/time")
    public String getTime() {
        //requests.inc();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String date = timestamp.toString();
        String email = userService.userName;
        if (personRepository.findPersonByPersonEmail(email) != null)
            return date;
        else
            return "Unauthorized";
    }

    @PostMapping(value = "/login")
    public String verifyPerson(@Valid @RequestBody Person person) throws AppException {
        List<Person> personList = personService.getAllPerson();
        for (Person p : personList) {
            if (p.getPersonEmail().equals(person.getPersonEmail())) {
                CharSequence verifyPassword = person.getPersonPassword();
                System.out.println(p.getPersonPassword());
                if (passwordEncoder.matches(verifyPassword, p.getPersonPassword())) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String date = timestamp.toString();
                    return date;
                }
            }
        }
        return "Please enter proper Credentials";
    }

    @GetMapping("/ping")
    public String getPing() {
        logger.info("-=-=--==INTO THE PING() method-=-=--=-=-");
        myOperationCounterSuccess.increment();
        return "You have hit the Ping Service.";
    }

    @GetMapping("/greet/{user}")
    public String getTest(@PathVariable("user") String user) {
        logger.info("-=-=--==INTO THE getTest() method-=-=--=-=-=-");
        String prefix = System.getenv().getOrDefault("amazonProperties.endpointUrl", "--IT IS DEFAULT VALUE--");
        myOperationCounterSuccess.increment();
        if (prefix == null) {
            prefix = "---PREFIX IS NUL----!";
        }

        String keyValue = env.getProperty("amazonProperties.bucketName");

        if (keyValue != null && !keyValue.isEmpty()) {
            return keyValue;
        } else {
            return String.format("%s %s! Welcome to Configuring Spring Boot on Kubernetes!", prefix, user);

        }
    }

    @GetMapping("/publish/{name}")
    public String post(@PathVariable("name") final String name) {

        //kafkaTemplate.send(TOPIC, name);
        System.out.println("--------NAME------------------------------------::::::"+name);
        kafkaTemplate.send(new ProducerRecord<>(TOPIC, name));

        //producer.send(new ProducerRecord<>(topic, payload));


        return "Published successfully";
    }

}
