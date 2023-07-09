package com.sreejith.customer;

import com.sreejith.exception.DuplicateResourceException;
import com.sreejith.exception.RequestValidationException;
import com.sreejith.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDao customerDao;

    private CustomerService undertest;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {

        undertest = new CustomerService(customerDao);
    }


    @Test
    void getAllCustomers() {
        //when
        undertest.getAllCustomers();

        //then
        verify(customerDao).selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        //Given
        long id = 1;
        Customer customer = new Customer("Alex", "Alex@gmailcom", 22);
        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));
        //when
        Customer actual = undertest.getCustomer((long) 1);

        //then
        assertEquals(actual, customer);

    }

    @Test
    void willThorwnWhenGetCustomerReturnsEmpty() {
        //Given
        long id = 1;
        when(customerDao.findCustomerByID(id)).thenReturn(Optional.empty());
        Class<ResourceNotFound> resourceNotFound = ResourceNotFound.class;

        //when
        Throwable exception = assertThrows(ResourceNotFound.class, () -> undertest.getCustomer((long) 1));

        //then
        String expectedMessage = "Customer with id: 1 not found.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Exception message should match");


    }

    @Test
    void addCustomer() {
        //Given
        AddCustomerDto addCustomerDto = new AddCustomerDto("Alex", "Alex@gmailcom", 22);
        when(customerDao.isExistingUser(addCustomerDto.email())).thenReturn(false);
        doNothing().when(customerDao).insertCustomer(any(Customer.class));
        //when
        undertest.addCustomer(addCustomerDto);

        //then
        verify(customerDao).isExistingUser(addCustomerDto.email());
        verify(customerDao).insertCustomer(any(Customer.class));

    }

    @Test
    void willThrownWhenaddCustomerHasExistingEmail() {
        //Given
        AddCustomerDto addCustomerDto = new AddCustomerDto("Alex", "Alex@gmailcom", 22);
        when(customerDao.isExistingUser(addCustomerDto.email())).thenReturn(true);
        //when
        Throwable exception = assertThrows(DuplicateResourceException.class, () -> undertest.addCustomer(addCustomerDto));

        //then
        verify(customerDao).isExistingUser(addCustomerDto.email());
        verify(customerDao, times(0)).insertCustomer(any(Customer.class));
        String expectedMessage = "email already in use.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Exception message should match");

    }

    @Test
    void deleteCustomer() {
        //Given
        long id = 1;
        Customer customer = new Customer("Alex", "Alex@gmailcom", 27);
        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerDao).deleteCustomer(id);
        //when
        undertest.deleteCustomer(id);

        //then
        verify(customerDao).findCustomerByID(id);
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowWhenDeleteCustomerWithNotExistingCustomer() {
        //Given
        long id = 1;
        when(customerDao.findCustomerByID(id)).thenReturn(Optional.empty());
        //when
        assertThrows(ResourceNotFound.class, () -> undertest.deleteCustomer(id));


        //then
        verify(customerDao).findCustomerByID(id);
        verify(customerDao, times(0)).deleteCustomer(id);
    }

    @Test
    void updateCustomerWhenAgeChanged() {
        //Given
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Alex", "Alex@gmailcom", 22);
        Customer customer = new Customer("Alex", "Alex@gmailcom", 27);
        long id = 1;

        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerDao).updateCustomer(customer);
        //when
        undertest.updateCustomer(updateCustomerDto, id);

        //then
        verify(customerDao).findCustomerByID(id);
        verify(customerDao).updateCustomer(customer);

    }

    @Test
    void updateCustomerWhenNameChanged() {
        //Given
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Alex", "Alex@gmailcom", 22);
        Customer customer = new Customer("Alexander", "Alex@gmailcom", 22);
        long id = 1;

        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerDao).updateCustomer(customer);
        //when
        undertest.updateCustomer(updateCustomerDto, id);

        //then
        verify(customerDao).findCustomerByID(id);
        verify(customerDao).updateCustomer(customer);

    }

    @Test
    void updateCustomerWhenEmailChanged() {
        //Given
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Alex", "Alex@gmailcom", 22);
        Customer customer = new Customer("Alex", "Alex22@gmailcom", 22);
        long id = 1;

        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));
        doNothing().when(customerDao).updateCustomer(customer);
        //when
        undertest.updateCustomer(updateCustomerDto, id);

        //then
        verify(customerDao).findCustomerByID(id);
        verify(customerDao).updateCustomer(customer);

    }

    @Test
    void willThrowUpdateCustomerWhenNotExistingCustomer() {
        //Given
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Alex", "Alex@gmailcom", 22);
        long id = 1;

        when(customerDao.findCustomerByID(id)).thenReturn(Optional.empty());
        //when
        Throwable exception = assertThrows(ResourceNotFound.class, () -> undertest.updateCustomer(updateCustomerDto, id));


        //then
        verify(customerDao).findCustomerByID(id);
        String expectedMessage = "Customer with id: %s not found.".formatted(id);
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Exception message should match");


    }

    @Test
    void willThrowUpdateCustomerWhenEmailInuse() {
        //Given
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Alex", "Alex@gmailcom", 22);
        Customer customer = new Customer("Alex", "Alex22@gmailcom", 27);
        long id = 1;

        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));
        when(customerDao.isExistingUser(updateCustomerDto.email())).thenReturn(true);

        //when
        Throwable exception = assertThrows(DuplicateResourceException.class, () -> undertest.updateCustomer(updateCustomerDto, id));


        //then
        verify(customerDao).findCustomerByID(id);
        verify(customerDao).isExistingUser(updateCustomerDto.email());
        String expectedMessage = "email already in use.";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Exception message should match");


    }

    @Test
    void willThrowUpdateCustomerWhenNoChanges() {
        //Given
        UpdateCustomerDto updateCustomerDto = new UpdateCustomerDto("Alex", "Alex@gmailcom", 22);
        Customer customer = new Customer("Alex", "Alex@gmailcom", 22);
        long id = 1;
        when(customerDao.findCustomerByID(id)).thenReturn(Optional.of(customer));

        //when
        Throwable exception = assertThrows(RequestValidationException.class, () -> undertest.updateCustomer(updateCustomerDto, id));

        //then
        verify(customerDao).findCustomerByID(id);
        String expectedMessage = "No Changes Found";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Exception message should match");


    }

}