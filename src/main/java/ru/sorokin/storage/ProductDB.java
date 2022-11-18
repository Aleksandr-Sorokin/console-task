package ru.sorokin.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sorokin.model.Customer;
import ru.sorokin.model.Product;
import ru.sorokin.model.dto.ProductDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Component
@RequiredArgsConstructor
public class ProductDB implements ProductStorage {
    private final JdbcTemplate jdbcTemplate;
    private PreparedStatement statement;

    @Override
    public void save(ProductDto productDto) {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            statement = connection
                    .prepareStatement("INSERT INTO product (title, price) VALUES (?,?);"
                            , Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, productDto.getTitle());
            statement.setDouble(2, productDto.getPrice());
            int answerRow = statement.executeUpdate();
            if (answerRow == 0) {
                throw new SQLException("Ошибка при добавлении продукта.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product findById(Long id) {
        List<Product> products = jdbcTemplate
                .query("SELECT * FROM product WHERE product_id = ?;", this::makeProduct, id);
        return products.get(0);
    }

    @Override
    public List<Product> findAll(List<Long> productId) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM product WHERE product_id IN (");
        productId.stream().forEach(aLong -> builder.append("?,"));
        builder.append(");");
        String sql = builder.toString().replace(",)", ")");
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()){
            statement = connection.prepareStatement(sql);
            int index = 1;
            for (int i = 0; i < productId.size(); i++, index++) {
                statement.setLong(index, productId.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(makeProduct(resultSet, resultSet.getRow()));
            }
            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM product WHERE product_id = ?;", ps -> ps.setLong(1, id));
    }

    private Product makeProduct(ResultSet rs, int rowNum) {
        try {
            Product product = new Product();
            product.setId(rs.getLong("product_id"));
            product.setTitle(rs.getString("title"));
            product.setPrice(rs.getDouble("price"));
            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
