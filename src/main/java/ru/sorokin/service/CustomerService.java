package ru.sorokin.service;

import ru.sorokin.model.StatisticPurchases;
import ru.sorokin.model.dto.CustomerDto;
import ru.sorokin.model.entity.Statistics;

import java.util.List;

public interface CustomerService {
    List<CustomerDto> findCustomerByLastName(String lastName);

    List<CustomerDto> findCustomerByProduct(String product, Integer limit);

    List<CustomerDto> findCustomerBetweenPrice(Integer min, Integer max);

    List<CustomerDto> findPassiveCustomer(Integer limit);

    StatisticPurchases findAllStatBetweenDate(Statistics statistics);
}
