package sreejith.customer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path="api/v1/customer")
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path="api/v1/customer/{id}")
    public Customer getCustomer(@PathVariable Integer id) {
        return customerService.getCustomer(id);
    }
}
