package sreejith.customer;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sreejith.exception.DuplicateResourceException;
import sreejith.exception.RequestValidationException;
import sreejith.exception.ResourceNotFound;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Long id) {
        return customerDao.findCustomerByID(id)
                .orElseThrow(() -> new ResourceNotFound("Customer with id: %s not found.".formatted(id)));
    }

    void addCustomer(AddCustomerDto addCustomerDto) {
        if (customerDao.isExistingUser(addCustomerDto.email())) {
            throw new DuplicateResourceException("email already in use.");
        }
        customerDao.insertCustomer(new Customer(addCustomerDto.name(),
                addCustomerDto.email(),
                addCustomerDto.age()));
    }

    void deleteCustomer(Long id) {
        customerDao.findCustomerByID(id).orElseThrow(() -> new ResourceNotFound("Customer with id: %s not found.".formatted(id)));
        customerDao.deleteCustomer(id);
    }

    void updateCustomer(UpdateCustomerDto updateCustomerDto, Long id) {
        Customer customer = customerDao.findCustomerByID(id).orElseThrow(
                () -> new ResourceNotFound("Customer with id: %s not found.".formatted(id)));
        boolean changes = false;
        if(!customer.getName().equals(updateCustomerDto.name())) {
            customer.setName(updateCustomerDto.name());
            changes = true;

        }
        if(!customer.getName().equals(updateCustomerDto.name())) {
            customer.setEmail(updateCustomerDto.email());
            changes = true;

        }
        if(!customer.getName().equals(updateCustomerDto.name())) {
            if (customerDao.isExistingUser(updateCustomerDto.email())) {
                throw new DuplicateResourceException("email already in use.");

            }
            customer.setAge(updateCustomerDto.age());
            changes = true;


        }
        if(!changes) {
            throw new RequestValidationException("No Changes Found");
        }
        customerDao.updateCustomer(customer);

    }
}
