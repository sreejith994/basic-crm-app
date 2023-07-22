package com.sreejith;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.sreejith.customer.Customer;
import com.sreejith.customer.CustomerRepository;

import java.util.Random;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);



    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            Faker faker = new Faker();
            var name = faker.name();
            Random rand = new Random();
            Customer customer = new Customer(name.fullName(),
                    faker.internet().safeEmailAddress(),
                    rand.nextInt(16,99),
                    rand.nextInt(1,10) >= 5 ? "male" : "female"
            );

            customerRepository.save(customer);

        };
    }

}
