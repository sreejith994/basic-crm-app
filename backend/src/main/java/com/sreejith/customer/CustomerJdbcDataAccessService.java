package com.sreejith.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJdbcDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    private final CustomerRowMapper customerRowMapper;

    public CustomerJdbcDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT * 
                FROM customer
                """;

        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> findCustomerByID(Long id) {
        var sql = """
                SELECT * 
                FROM customer
                WHERE id = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age,gender)
                VALUES (?,?,?,?)
                """;
        jdbcTemplate.update(sql,
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender());
    }

    @Override
    public Boolean isExistingUser(String email) {
        var sql = """
                SELECT * 
                FROM customer
                WHERE email = ?
                """;

        return jdbcTemplate.query(sql, customerRowMapper, email)
                .stream()
                .findFirst()
                .isPresent();
    }

    @Override
    public void deleteCustomer(Long id) {
        var sql = """
                    DELETE
                    FROM customer
                    WHERE id = ?
                """;

        jdbcTemplate.update(sql, id);

    }

    @Override
    public void updateCustomer(Customer update) {
        var sql = """
            UPDATE customer
            SET name = ?,
            email = ?,
            age = ?,
            gender =?
            WHERE id = ?
            """;

        jdbcTemplate.update(sql, update.getName(), update.getEmail(), update.getAge(),update.getGender() ,update.getId());
    }


}
