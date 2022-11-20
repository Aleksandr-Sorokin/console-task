package ru.sorokin.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.sorokin.config.ModelMapperUtil;
import ru.sorokin.model.Customer;
import ru.sorokin.model.dto.CustomerDto;
import ru.sorokin.storage.CustomerStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final ModelMapper mapper;

    private final CustomerStorage customerStorage;

    @Override
    public List<CustomerDto> findCustomerByLastName(String lastName) {
        if (lastName != null) {
            List<Customer> customers = customerStorage.findCustomerByLastName(lastName);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDto> findCustomerByProduct(String product, Integer limit) {
        if (product != null && limit != null) {
            List<Customer> customers = customerStorage.findCustomerByProduct(product, limit);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDto> findCustomerBetweenPrice(Integer min, Integer max) {
        if (min != null && max != null) {
            List<Customer> customers = customerStorage.findCustomerBetweenPrice(min, max);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    @Override
    public List<CustomerDto> findPassiveCustomer(Integer limit) {
        if (limit != null) {
            List<Customer> customers = customerStorage.findPassiveCustomer(limit);
            return ModelMapperUtil.convertList(customers, this::convertToDto);
        }
        return new ArrayList<>();
    }

    private CustomerDto convertToDto(Customer customer) {
        return mapper.map(customer, CustomerDto.class);
    }
}
