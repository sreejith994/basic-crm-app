package sreejith.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping(path="{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    public void registerCustomer(@RequestBody AddCustomerDto addCustomerDto) {
        customerService.addCustomer(addCustomerDto);
    }

    @PutMapping(path="{id}")
    public void updateCustomer(@RequestBody UpdateCustomerDto updateCustomerDto, @PathVariable Long id) {
        customerService.updateCustomer(updateCustomerDto,  id );
    }

    @DeleteMapping(path="{id}")
    public void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
