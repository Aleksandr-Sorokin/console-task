package ru.sorokin.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sorokin.model.Customer;
import ru.sorokin.model.dto.CustomerDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerDB implements CustomerStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<Customer> findCustomerByLastName(String lastName) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT * FROM customer WHERE last_name = ?;", this::makeCustomer, lastName);
        return customers;
    }

    public List<Customer> findCustomerByProduct(String product, Integer limit) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT DISTINCT customer.customer_id, customer.first_name, customer.last_name " +
                        "FROM customer " +
                        "JOIN purchase ON customer.customer_id = purchase.customer_id " +
                        "JOIN product ON product.product_id = purchase.product_id " +
                        "WHERE product.title = ? AND purchase.volume >= ?;", this::makeCustomer, product, limit);
        return customers;
    }

    public List<Customer> findCustomerBetweenPrice(Integer min, Integer max) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT customer.customer_id, customer.last_name, customer.first_name " +
                        "FROM customer " +
                        "LEFT JOIN purchase ON purchase.customer_id = customer.customer_id " +
                        "LEFT JOIN product ON product.product_id = purchase.product_id " +
                        "GROUP BY customer.customer_id " +
                        "HAVING SUM(purchase.volume * product.price) BETWEEN ? AND ?;", this::makeCustomer, min, max);
        return customers;
    }
    public List<Customer> findPassiveCustomer(Integer limit) {
        return null;
    }

    @Override
    public void save(CustomerDto customerDto) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO customer (first_name, last_name) VALUES (?,?);"
                            ,Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, customerDto.getFirstName());
            statement.setString(2, customerDto.getLastName());
            int answerRow = statement.executeUpdate();
            if (answerRow == 0) {
                throw new SQLException("Ошибка при добавлении покупателя.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer findById(Long id) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT * FROM customer WHERE customer_id = ?;", this::makeCustomer, id);
        return customers.get(0);
    }

    @Override
    public List<Customer> findAll(List<Long> customerId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM customer WHERE customer_id IN (");
        customerId.stream().forEach(aLong -> builder.append("?,"));
        builder.append(");");
        String sql = builder.toString().replace(",)", ")");
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            int index = 1;
            for (int i = 0; i < customerId.size(); i++, index++) {
                statement.setLong(index, customerId.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                customers.add(makeCustomer(resultSet, resultSet.getRow()));
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAllByLastName(String lastName) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT * FROM customer WHERE last_name = ?;", this::makeCustomer, lastName);
        return customers;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM customer WHERE customer_id = ?;", ps -> ps.setLong(1, id));
    }

    private Customer makeCustomer(ResultSet rs, int rowNum) {
        try {
            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setFirstName(rs.getString("last_name"));
            customer.setLastName(rs.getString("last_name"));
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
