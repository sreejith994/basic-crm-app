package com.sreejith.customer;

import com.sreejith.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestcontainers {

    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void existsCustomerByEmail() {
        //when
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                "male"

        );
        underTest.save(customer);

        //then
        var result = underTest.existsCustomerByEmail( email);
        assertThat(result).isTrue();

    }

    @Test
    void existsCustomerByEmail_FailsWhenEmailNotPresnt() {
        //when
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();


        //then
        var result = underTest.existsCustomerByEmail( email);
        assertThat(result).isFalse();

    }

    @Test
    void existsCustomerById() {
        //when
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                "male"
        );
        underTest.save(customer);
        long id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow()
                .getId();

        //then
        var result = underTest.existsCustomerById(id);
        assertThat(result).isTrue();

    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        //then
        var result = underTest.existsCustomerById((long) -1);
        assertThat(result).isFalse();

    }
}