package sreejith.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CustomerDataAccessService implements CustomerDao {

    List<Customer> customerList = new ArrayList<>();
    Customer alex = new Customer(1,"a","e",21);
    Customer jamila = new Customer(2,"b","e",22);

    public CustomerDataAccessService() {
        customerList.add(alex);
        customerList.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customerList;
    }

    @Override
    public Optional<Customer> findCustomerByID(Integer id) {
        return customerList.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();

    }
}
