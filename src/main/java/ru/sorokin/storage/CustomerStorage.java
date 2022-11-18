package ru.sorokin.storage;

import ru.sorokin.model.Customer;
import ru.sorokin.model.dto.CustomerDto;

import java.util.List;

public interface CustomerStorage {
    void save(CustomerDto customerDto);
    Customer findById(Long id);
    List<Customer> findAll(List<Long> customers);
    List<Customer> findAllByLastName(String lastName);
    void delete(Long id);
}
