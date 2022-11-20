package ru.sorokin.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sorokin.model.Customer;
import ru.sorokin.model.Product;
import ru.sorokin.model.StatisticBetweenDate;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerDB implements CustomerStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Customer> findCustomerByLastName(String lastName) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT * FROM customer WHERE last_name = ?;", this::makeCustomer, lastName);
        return customers;
    }

    @Override
    public List<Customer> findCustomerByProduct(String product, Integer limit) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT DISTINCT customer.customer_id, customer.first_name, customer.last_name " +
                        "FROM customer " +
                        "JOIN purchase ON customer.customer_id = purchase.customer_id " +
                        "JOIN product ON product.product_id = purchase.product_id " +
                        "WHERE product.title = ? AND purchase.volume >= ?;", this::makeCustomer, product, limit);
        return customers;
    }

    @Override
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

    @Override
    public List<Customer> findPassiveCustomer(Integer limit) {
        List<Customer> customers = jdbcTemplate
                .query("SELECT customer.customer_id, customer.first_name, customer.last_name, " +
                        "SUM(purchase.volume) AS total " +
                        "FROM purchase " +
                        "JOIN customer ON customer.customer_id = purchase.customer_id " +
                        "GROUP BY customer.customer_id ORDER BY total LIMIT ?;", this::makeCustomer, limit);
        return customers;
    }

    @Override
    public List<StatisticBetweenDate> findAllStatBetweenDate(List<LocalDate> dates) {
        List<StatisticBetweenDate> statistic = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT product.product_id, product.price, product.title, " +
                    "SUM(product.price * purchase.volume), " +
                    "purchase.customer_id, customer.first_name, customer.last_name " +
                    "FROM product JOIN purchase ON product.product_id = purchase.product_id " +
                    "JOIN customer ON customer.customer_id = purchase.customer_id " +
                    "WHERE purchase.purchase_date IN (");
            dates.stream().forEach(date -> builder.append("?,"));
            builder.append(") GROUP BY product.product_id, purchase.customer_id, customer.first_name, customer.last_name " +
                    "ORDER BY purchase.customer_id, SUM(product.price * purchase.volume);");
            String sql = builder.toString().replace(",)", ")");
            PreparedStatement statement = connection.prepareStatement(sql);
            int index = 1;
            for (int i = 0; i < dates.size(); i++, index++) {
                statement.setDate(index, Date.valueOf(dates.get(i)));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                statistic.add(makeStatisticPurchases(resultSet, resultSet.getRow()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statistic;
    }

    private StatisticBetweenDate makeStatisticPurchases(ResultSet rs, int rowNum) {
        try {
            Product product = new Product();
            Customer customer = new Customer();
            StatisticBetweenDate statistic = new StatisticBetweenDate();
            product.setId(rs.getLong("product_id"));
            product.setPrice(rs.getDouble("price"));
            product.setTitle(rs.getString("title"));
            customer.setId(rs.getLong("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            statistic.setId(rowNum);
            statistic.setProduct(product);
            statistic.setCustomer(customer);
            statistic.setSum(rs.getDouble("sum"));
            return statistic;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Customer makeCustomer(ResultSet rs, int rowNum) {
        try {
            Customer customer = new Customer();
            customer.setId(rs.getLong("customer_id"));
            customer.setFirstName(rs.getString("first_name"));
            customer.setLastName(rs.getString("last_name"));
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
