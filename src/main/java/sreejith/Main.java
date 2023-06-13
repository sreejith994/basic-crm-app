package sreejith;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import sreejith.customer.Customer;
import sreejith.customer.CustomerRepository;

import java.util.Arrays;

@SpringBootApplication
public class Main {


    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);


    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository) {
        return args -> {
            Customer john = new Customer("John","john.smith@amz.com",34);
            Customer alex = new Customer("alex","alex.smith@amz.com",37);

            customerRepository.save(john);
            customerRepository.save(alex);

        };
    }

}
