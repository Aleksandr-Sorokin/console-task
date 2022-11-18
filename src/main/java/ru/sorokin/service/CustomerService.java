package ru.sorokin.service;

import ru.sorokin.model.Customer;
import ru.sorokin.model.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    void save(CustomerDto customerDto);
    Customer findById(Long id);
    Customer updateById(Long id, CustomerDto customerDto);
    Customer findAll(List<Long> customers);
    Customer delete(Long id);
}
