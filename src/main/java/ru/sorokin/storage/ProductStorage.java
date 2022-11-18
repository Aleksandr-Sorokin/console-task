package ru.sorokin.storage;

import ru.sorokin.model.Product;
import ru.sorokin.model.dto.ProductDto;

import java.util.List;

public interface ProductStorage {
    void save(ProductDto productDto);
    Product findById(Long id);
    List<Product> findAll(List<Long> productId);
    void delete(Long id);
}
