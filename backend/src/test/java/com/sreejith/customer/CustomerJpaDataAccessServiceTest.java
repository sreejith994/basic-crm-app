package com.sreejith.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJpaDataAccessServiceTest {

    private CustomerJpaDataAccessService underTest;

    private AutoCloseable autoCloseable;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //when
        underTest.selectAllCustomers();

        //then
        Mockito.verify(customerRepository)
                .findAll();

    }

    @Test
    void findCustomerByID() {
        //given
        long id =1;
        //when
        underTest.findCustomerByID(id);

        //then
        Mockito.verify(customerRepository)
                .findById(id);

    }

    @Test
    void isExistingUser() {
        //given
        String email = "test";
        //when
        underTest.isExistingUser(email);

        //then
        Mockito.verify(customerRepository)
                .existsCustomerByEmail(email);

    }

    @Test
    void deleteCustomer() {
        //given
        long id =1;
        //when
        underTest.deleteCustomer(id);

        //then
        Mockito.verify(customerRepository)
                .deleteById(id);

    }

    @Test
    void updateCustomer() {
        //given
        Customer customer = new Customer();
        //when
        underTest.updateCustomer(customer);

        //then
        Mockito.verify(customerRepository)
                .save(customer);

    }

    @Test
    void insertCustomer() {
        //given
        Customer customer = new Customer();
        //when
        underTest.updateCustomer(customer);

        //then
        Mockito.verify(customerRepository)
                .save(customer);

    }
}