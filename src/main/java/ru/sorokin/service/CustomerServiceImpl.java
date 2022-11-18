package ru.sorokin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sorokin.model.Customer;
import ru.sorokin.model.dto.CustomerDto;
import ru.sorokin.storage.CustomerDB;
import ru.sorokin.storage.CustomerStorage;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerStorage customerStorage;

    @Override
    public void save(CustomerDto customerDto) {
        customerStorage.save(customerDto);
    }

    @Override
    public Customer findById(Long id) {
        return null;
    }

    @Override
    public Customer updateById(Long id, CustomerDto customerDto) {
        return null;
    }

    @Override
    public Customer findAll(List<Long> customers) {
        return null;
    }

    @Override
    public Customer delete(Long id) {
        return null;
    }
}
