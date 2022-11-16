package ru.sorokin.service;

import ru.sorokin.model.dto.CustomerDto;

import java.util.List;

public interface CriteriaService {
    List<CustomerDto> findCustomerByLastName(String lastName);

    List<CustomerDto> findCustomerByProduct(String product, Integer limit);

    List<CustomerDto> findCustomerBetweenPrice(Double min, Double max);

    List<CustomerDto> findPassiveCustomer(Integer limit);
}
