package com.sreejith.customer;

import com.sreejith.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJdbcDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        //Given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID(),
                20, "male"
        );

        underTest.insertCustomer(customer);
        //when
        List<Customer> customers = underTest.selectAllCustomers();
        //then
        assertThat(customers).isNotEmpty();

    }

    @Test
    void findCustomerByID() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                "male"
        );

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(c -> c.getId())
                .findFirst()
                .orElseThrow();

        //when

        Optional<Customer> result = underTest.findCustomerByID(id);


        //then
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerByID() {
        //Given
        int id = -1;
        //when
        Optional<Customer> result = underTest.findCustomerByID((long) id);

        //then
        assertThat(result).isNotPresent();

    }

    @Test
    void insertCustomer() {
        //Given

        //when
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow()
                .getId();

        //then
        Optional<Customer> result = underTest.findCustomerByID(id);
        assertThat(result).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void isExistingUser() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );
        underTest.insertCustomer(customer);

        //when
        Boolean isExistingUser = underTest.isExistingUser(email);

        //then
        assertThat(isExistingUser).isTrue();
    }

    @Test
    void deleteCustomer() {
        //Given
        String email = FAKER.internet().safeEmailAddress() + "_" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );
        underTest.insertCustomer(customer);
        long id = underTest.selectAllCustomers().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .orElseThrow()
                .getId();

        //when
        underTest.deleteCustomer(id);
        Optional<Customer> result = underTest.findCustomerByID(id);

        //then
        assertThat(result).isNotPresent();

    }

    @Test
    void updateCustomerName() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );

        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "foo";

        // When age is name
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(email);
        update.setAge(20);
        update.setName(newName);
        update.setGender("male");

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerByID(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName); // change
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );

        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        ;

        // When email is changed
        Customer update = new Customer();
        update.setId(id);
        update.setName(customer.getName());
        update.setEmail(newEmail);
        update.setAge(20);
        update.setGender("male");


        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerByID(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(newEmail); // change
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );

        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 100;

        // When age is changed
        Customer update = new Customer();
        update.setId(id);
        update.setName(customer.getName());
        update.setEmail(email);
        update.setAge(newAge);
        update.setGender("male");


        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerByID(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(newAge); // change
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );

        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update with new name, age and email
        Customer update = new Customer();
        update.setId(id);
        update.setName("foo");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(22);
        update.setGender("male");

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerByID(id);

        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20, "male"
        );

        underTest.insertCustomer(customer);

        long id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When update without no changes
        Customer update = new Customer();
        update.setId(id);
        update.setName(customer.getName());
        update.setEmail(customer.getEmail());
        update.setAge(customer.getAge());
        update.setGender("male");


        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.findCustomerByID(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
        });
    }
}