package com.sreejith.journey;

import com.github.javafaker.Faker;
import com.sreejith.customer.AddCustomerDto;
import com.sreejith.customer.Customer;
import com.sreejith.customer.UpdateCustomerDto;
import com.sreejith.exception.ResourceNotFound;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String CUSTOMER_URI =  "/api/v1/customer";

    @Test
    void canRegisterCustomer() {
        // create registration request
        Faker faker = new Faker();
        String name = faker.name().name();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();
        int age = faker.number().numberBetween(15, 99);
        AddCustomerDto request = new AddCustomerDto(
                name,
                email,
                age
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AddCustomerDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();

        //make sure that customer is present
        Customer expectedCustomer = new Customer(name,email,age);

        assertThat(allCustomers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        //get customer by id

        long id = getIdFromCustomerByEmail(allCustomers,email);
        expectedCustomer.setId(id);
         webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        // create registration request
        Faker faker = new Faker();
        String name = faker.name().name();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();
        int age = faker.number().numberBetween(15, 99);
        AddCustomerDto request = new AddCustomerDto(
                name,
                email,
                age
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AddCustomerDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        long id = getIdFromCustomerByEmail(allCustomers,email);

        //delete customer
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();


        //get customer by id

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }



    @Test
    void canUpdateCustomer() {
        // create registration request
        Faker faker = new Faker();
        String name = faker.name().name();
        String email = UUID.randomUUID() + faker.internet().safeEmailAddress();
        int age = faker.number().numberBetween(15, 99);
        AddCustomerDto request = new AddCustomerDto(
                name,
                email,
                age
        );

        //send post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), AddCustomerDto.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();


        long id = getIdFromCustomerByEmail(allCustomers,email);

        //update customer

        UpdateCustomerDto update = new UpdateCustomerDto(
                name, email, age +1
        );
        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(update), UpdateCustomerDto.class)
                .exchange()
                .expectStatus()
                .isOk();


        //get customer by id

        Customer actual = webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody()
        ;

        assertThat(actual.getAge()).isEqualTo(age+1);

    }

    long getIdFromCustomerByEmail(List<Customer> customers,String email) {
        return customers.stream()
                .filter(c -> email.equals(c.getEmail()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("user not found"))
                .getId();
    }



}
