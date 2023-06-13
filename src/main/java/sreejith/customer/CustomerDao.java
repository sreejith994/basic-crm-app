package sreejith.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    List<Customer> selectAllCustomers();

    Optional<Customer> findCustomerByID(Integer id);

    void insertCustomer(Customer customer);

    Boolean isExistingUser(String email);

    void deleteCustomer(Integer id);

    void updateCustomer(Customer update);


}
