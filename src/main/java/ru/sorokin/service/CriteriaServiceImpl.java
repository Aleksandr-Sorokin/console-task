package ru.sorokin.service;

import ru.sorokin.model.dto.CustomerDto;

import java.util.List;

public class CriteriaServiceImpl implements CriteriaService{

    @Override
    public List<CustomerDto> findCustomerByLastName(String lastName) {
        System.out.println(lastName);
        return null;
    }

    @Override
    public List<CustomerDto> findCustomerByProduct(String product, Integer limit) {
        return null;
    }

    @Override
    public List<CustomerDto> findCustomerBetweenPrice(Double min, Double max) {
        return null;
    }

    @Override
    public List<CustomerDto> findPassiveCustomer(Integer limit) {
        return null;
    }
}
